package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class DbAuthDAO implements AuthDAO {

    public DbAuthDAO() throws SQLException {
        String createAuthTable = """
            CREATE TABLE IF NOT EXISTS authData (
            'token' VARCHAR(255) NOT NULL,
            'username' VARCHAR(255) NOT NULL,
            PRIMARY KEY ('token'))""";
    }

    @Override
    public void createAuth(AuthData authData) {

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
}
