package database;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.UserData;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test //passes
    public void passCreateUser() {
        UserData user = new UserData("name", "pass", "mail");
        userDAO.createUser(user);
        UserData result = userDAO.getUserByUsername("name");
        assertNotNull(result);
        assertEquals("name", result.username());
        assertEquals("pass", result.password());
        assertEquals("mail", result.email());
    }

    @Test
    public void failCreateUser() {
        UserData user = new UserData("name", "pass", null);
        assertThrows(RuntimeException.class, () -> userDAO.createUser(user));
    }

    @Test //passes... looks totally different to the create test
    public void passGetUser() {
        UserData user = new UserData("totallydifferentname", "pass", "mail");
        userDAO.createUser(user);
        UserData result = userDAO.getUserByUsername("totallydifferentname");
        assertNotNull(result);
        assertEquals("totallydifferentname", result.username());
        assertEquals("pass", result.password());
        assertEquals("mail", result.email());
    }

    @Test //passes
    public void failGetUser() {
        UserData result = userDAO.getUserByUsername("invalidUsername");
        assertNull(result);
    }

    @Test //passes
    public void passClearUser() {
        UserData user = new UserData("name", "pass", "mail");
        userDAO.createUser(user);
        UserData result = userDAO.getUserByUsername("name");
        assertNotNull(result);
        userDAO.clear();
        assertNull(userDAO.getUserByUsername("name"));
    }

    @Test //passes
    public void passCreateAuth() {
        String token = "validToken";
        AuthData authData = new AuthData(token, "validUsername");
        authDAO.createAuth(authData);
        AuthData result = authDAO.getAuthByToken("validToken");
        assertEquals(token, result.authToken());
        assertEquals("validUsername", result.username());
    }

    @Test //passes
    public void failCreateAuth() {
        AuthData authData = new AuthData(null, "invalidUsername");
        assertThrows(RuntimeException.class, () -> authDAO.createAuth(authData));
    }

    @Test //passes
    public void passGetAuth() {
        authDAO.createAuth(new AuthData("validToken", "validUsername"));
        AuthData result = authDAO.getAuthByToken("validToken");
        assertEquals("validUsername", result.username());
        assertEquals("validToken", result.authToken());
    }

    @Test //passes
    public void failGetAuth() {
        AuthData result = authDAO.getAuthByToken("invalidToken");
        assertNull(result);
    }

    @Test //passes
    public void passDeleteAuth() {
        authDAO.createAuth(new AuthData("validToken", "validUsername"));
        AuthData result = authDAO.getAuthByToken("validToken");
        assertNotNull(result);
        authDAO.deleteAuth(result);
        assertNull(authDAO.getAuthByToken("validToken"));
    }

    @Test //passes
    public void failDeleteAuth() {
        assertThrows(RuntimeException.class, () -> authDAO.deleteAuth(null));
    }

    @Test //passes
    public void passClearAuth() {
        authDAO.createAuth(new AuthData("validToken", "validUsername"));
        assertNotNull(authDAO.getAuthByToken("validToken"));
        authDAO.clear();
        assertNull(authDAO.getAuthByToken("validToken"));
    }

    @Test //passes
    public void passCreateGame() {
        int gameID = gameDAO.createGame(new GameData(300, "white",
                null, "name", new ChessGame()));
        GameData result = gameDAO.getGameByID(gameID);
        assertNotNull(result);
        assertEquals("white", result.whiteUsername());
        assertEquals("name", result.gameName());
        assertNull(result.blackUsername());
    }

    @Test //passes
    public void failCreateGame() {
        int gameID = gameDAO.createGame(new GameData(1000, null, null, "name", new ChessGame()));
        GameData result = gameDAO.getGameByID(-1);
        assertNull(result);
    }

    @Test
    public void passGetGame() {
        //haha good one i aint tryin this yet
    }

    @Test
    public void failGetGame() {
        //samesies
    }

    @Test
    public void passClearGame() {
        //bro i cant create the games
    }

    @Test
    public void passUpdateGame() {
        //no can do
    }

    @Test
    public void failUpdateGame() {
        //soon i promise ill get this figured out. i think
    }

    @Test
    public void passGetAllGames() {
        //cant get one game let alone all
    }

    @Test
    public void failGetAllGames() {
        //i certainly do fail to get all games
    }
}
