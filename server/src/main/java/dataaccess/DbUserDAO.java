package dataaccess;

import model.UserData;

import java.sql.SQLException;

public class DbUserDAO implements UserDAO {
    public DbUserDAO() throws SQLException {
        String createAuthTable = """
            CREATE TABLE IF NOT EXISTS userData (
            'username' VARCHAR(256) NOT NULL,
            'password' VARCHAR(256) NOT NULL,
            'email' VARCHAR(256) NOT NULL,
            PRIMARY KEY('username'))""";
    }

    @Override
    public void createUser(UserData user) {

    }

    @Override
    public UserData getUserByUsername(String username) {
        return null;
    }

    @Override
    public void clear() {

    }
}
