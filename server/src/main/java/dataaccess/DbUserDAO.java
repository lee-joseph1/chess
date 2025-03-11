package dataaccess;

import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DbUserDAO implements UserDAO {
    public DbUserDAO() {
        String createUserTable = """
            CREATE TABLE IF NOT EXISTS userData (
            'username' VARCHAR(256) NOT NULL,
            'password' VARCHAR(256) NOT NULL,
            'email' VARCHAR(256) NOT NULL,
            PRIMARY KEY('username'))""";
        try {
            Connection conn = DatabaseManager.getConnection();
            Statement stmt = conn.createStatement();
            stmt.execute(createUserTable);
        }
        catch(Exception exception) {
            System.out.println(exception.getMessage());
        }
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
