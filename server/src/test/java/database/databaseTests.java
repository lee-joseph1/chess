package database;

import dataaccess.*;
import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class databaseTests {
    private static DbAuthDAO authDAO;
    private static DbUserDAO userDAO;
    private static DbGameDAO gameDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        try {
            authDAO = new DbAuthDAO();
            userDAO = new DbUserDAO();
            gameDAO = new DbGameDAO();
        } catch (DataAccessException ex) {
            throw new DataAccessException("error setting up a test");
        }
    }

    @AfterEach
    public void reset() {
        authDAO.clear();
        userDAO.clear();
        gameDAO.clear();
    }

    @Test
    public void passCreateUser() {

    }

    @Test
    public void failCreateUser() {

    }

    @Test
    public void passGetUser() {

    }

    @Test
    public void failGetUser() {

    }

    @Test
    public void passClearUser() {

    }

    @Test
    public void failClearUser() {

    }

    @Test
    public void passCreateAuth() {
        String token = "validToken";
        AuthData authData = new AuthData(token, "validUsername");
        authDAO.createAuth(authData);
        AuthData result = authDAO.getAuthByToken("validToken");
        assertEquals(token, result.authToken());
        assertEquals("validUsername", result.username());
    }

    @Test
    public void failCreateAuth() {

    }

    @Test
    public void passGetAuth() {

    }

    @Test
    public void failGetAuth() {

    }

    @Test
    public void passDeleteAuth() {

    }

    @Test
    public void failDeleteAuth() {

    }

    @Test
    public void passClearAuth() {

    }

    @Test
    public void failClearAuth() {

    }

    @Test
    public void passCreateGame() {

    }

    @Test
    public void failCreateGame() {

    }

    @Test
    public void passGetGame() {

    }

    @Test
    public void failGetGame() {

    }

    @Test
    public void passClearGame() {

    }

    @Test
    public void failClearGame() {

    }

    @Test
    public void passUpdateGame() {

    }

    @Test
    public void failUpdateGame() {

    }

    @Test
    public void passGetAllGames() {

    }

    @Test
    public void failGetAllGames() {

    }
}
