package dataaccess;

import model.AuthData;
import passoff.exception.ResponseParseException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static java.sql.Types.NULL;

public class DbAuthDAO implements AuthDAO {

    public DbAuthDAO() throws DataAccessException {
        configureDatabase();
    }


    @Override
    public void createAuth(AuthData authData) {
        var stmt = "INSERT INTO authData (token, username) VALUES (?, ?)";
        var username = authData.username();
        try {
            executeUpdate(stmt, authData.authToken(), username);
        }
        catch (SQLException | DataAccessException e) {
            throw new RuntimeException("Error creating auth");
        }
    }

    @Override
    public void clear() {

    }

    @Override
    public AuthData getAuthByToken(String authToken) {
        return null;
    }

    @Override
    public void deleteAuth(AuthData authData) {

    }

    @Override
    public String generateUniqueToken() {
        return "";
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS authData (
            'token' VARCHAR(255) NOT NULL,
            'username' VARCHAR(255) NOT NULL,
            PRIMARY KEY ('token'))
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
        } catch (SQLException ex) {
            throw new DataAccessException("Error creating database");
        }
    }

    private void executeUpdate(String stmt, Object... params) throws SQLException, DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(stmt)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();
//                var rs = ps.getGeneratedKeys();
//                if (rs.next()) {
//                    rs.getInt(1);
//                }i dont see how this part is used soooo im leaving it out unless necessary
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to update database: " + ex.getMessage());
        }
    }
}
