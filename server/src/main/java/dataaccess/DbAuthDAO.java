package dataaccess;

import model.AuthData;
import passoff.exception.ResponseParseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import dataaccess.DataAccessException;
import java.sql.Statement;
import java.util.UUID;

import static java.sql.Types.NULL;

public class DbAuthDAO implements AuthDAO {

    public DbAuthDAO() throws DataAccessException {
        configureDatabase();
    }


    @Override//addPet
    public void createAuth(AuthData authData) {
        var stmt = "INSERT INTO authData (token, username) VALUES (?, ?)";
        var username = authData.username();
        var token = authData.authToken();
        try {
            executeUpdate(stmt, token, username);
        }
        catch (Exception ex) {
            throw new RuntimeException("Error creating auth: " + ex.getMessage());
        }
    }

    @Override//deleteAllPets
    public void clear() {
        var stmt = "TRUNCATE TABLE authData";
        try {
            executeUpdate(stmt);
        }
        catch (Exception ex) {
            throw new RuntimeException("Error clearing auth: " + ex.getMessage());
        }
    }

    @Override//getPet
    public AuthData getAuthByToken(String authToken) {
        try (var conn = DatabaseManager.getConnection()) {
            var stmt = "SELECT * FROM authData WHERE token = ?";
            try (var ps = conn.prepareStatement(stmt)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new AuthData(authToken, rs.getString("username"));
                    }
                }
            }
        } catch (DataAccessException | SQLException ex) {
            throw new RuntimeException("Error getting auth by token: " + ex.getMessage());
        }
        return null;
    }

    @Override//deletePet
    public void deleteAuth(AuthData authData) {
        try {
            var stmt = "DELETE FROM authData WHERE token = ?";
            executeUpdate(stmt, authData.authToken());
        }
        catch (Exception ex) {
            throw new RuntimeException("Error deleting auth: " + ex.getMessage());
        }
    }

    @Override
    public String generateUniqueToken() {
        return UUID.randomUUID().toString();
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS authData (
            `token` VARCHAR(255) NOT NULL,
            `username` VARCHAR(255) NOT NULL,
            PRIMARY KEY (`token`))
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var stmt : createStatements) {
                try (var ps = conn.prepareStatement(stmt)) {
                    ps.executeUpdate();
                }
            }
    } catch (DataAccessException | SQLException ex) {
            throw new RuntimeException("Error creating auth database");
        }
    }

    private void executeUpdate(String stmt, Object... params) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();
                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    rs.getInt(1);
                }
            }
        } catch (DataAccessException | SQLException ex) {
            throw new RuntimeException("Failed to update database: " + ex.getMessage());
        }
    }
}
