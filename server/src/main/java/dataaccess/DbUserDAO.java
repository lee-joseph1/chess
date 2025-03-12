package dataaccess;

import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static java.sql.Types.NULL;

public class DbUserDAO implements UserDAO {

    public DbUserDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void createUser(UserData user) {
        var stmt = "INSERT INTO userData (username, password, email) VALUES (?, ?, ?)";
        var username = user.username();
        var password = user.password();
        var email = user.email();
        try {
            executeUpdate(stmt, username, password, email);
        }
        catch (Exception ex) {
            throw new RuntimeException("Error creating user: " + ex.getMessage());
        }
    }

    @Override
    public UserData getUserByUsername(String username) {
        try (var conn = DatabaseManager.getConnection()) {
            var stmt = "SELECT * FROM userData WHERE username = ?";
            try (var ps = conn.prepareStatement(stmt)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new UserData(username, rs.getString("password"), rs.getString("email"));
                    }
                }
            }
        } catch (DataAccessException | SQLException ex) {
            throw new RuntimeException("Error getting user by username: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public void clear() {
        var stmt = "TRUNCATE TABLE userData";
        try {
            executeUpdate(stmt);
        }
        catch (Exception ex) {
            throw new RuntimeException("Error clearing user: " + ex.getMessage());
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS userData (
            `username` VARCHAR(256) NOT NULL,
            `password` VARCHAR(256) NOT NULL,
            `email` VARCHAR(256) NOT NULL,
            PRIMARY KEY(`username`))
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (String stmt : createStatements) {
                try (var ps = conn.prepareStatement(stmt)) {
                    ps.executeUpdate();
                }
            }
        } catch (DataAccessException | SQLException ex) {
            throw new RuntimeException("Error creating user database");
        }
    }

    private void executeUpdate(String stmt, Object... params) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(stmt)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();
            }
        } catch (DataAccessException | SQLException ex) {
                throw new RuntimeException("Failed to update database");
        }
    }
}
