package client;

import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import model.AuthData;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    public static ServerFacade facade;

    @BeforeAll
    public static void init() throws Exception {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
        facade.clear();
    }

    @AfterAll
    static void stopServer() throws Exception {
        facade.clear();
        server.stop();
    }


    @Test
    public void registerPass() throws Exception {
        String[] args = new String[]{"username1", "password1", "email1"};
        AuthData authData = facade.register(args);
        assertNotNull(authData.authToken());
    }

    @Test
    public void registerFail() {
        assertThrows(Exception.class, () -> {facade.register(new String[]{null, "password", "email"});});
    }

    @Test
    public void loginPass() throws Exception {
        String[] args = new String[]{"username2", "password2", "email2"};
        AuthData authData = facade.register(args);
        facade.logout(authData);
        AuthData auth = facade.login(new String[]{"username2", "password2"});
        assertNotNull(auth.authToken());
    }

    @Test
    public void loginFail() {
        assertThrows(Exception.class, () -> {facade.login(new String[]{null, "password"});});
    }

    @Test
    public void logoutPass() throws Exception {
        String[] args = new String[]{"username3", "password3", "email3"};
        AuthData authData = facade.register(args);
        facade.logout(authData);
        assertThrows(Exception.class, () -> {facade.create(authData, "no3");});
    }

    @Test
    public void logoutFail() {
        assertThrows(Exception.class, () -> {facade.logout(null);});
    }

    @Test
    public void createPass() throws Exception {
        String[] args = new String[]{"username4", "password4", "email4"};
        AuthData authData = facade.register(args);
        facade.logout(authData);
        AuthData auth = facade.login(new String[]{"username4", "password4"});
        GameData game = facade.create(auth, "gameName4");
        assertNotNull(game);
    }

    @Test
    public void createFail() {
        assertThrows(Exception.class, () -> {facade.create(null, "no");});
    }

    @Test
    public void listPass() throws Exception {
        String[] args = new String[]{"username5", "password5", "email5"};
        AuthData authData = facade.register(args);
        facade.logout(authData);
        AuthData auth = facade.login(new String[]{"username5", "password5"});
        facade.create(auth, "gameName5");
        assertFalse(facade.list(auth).games().isEmpty());
    }

    @Test
    public void listFail() {
        assertThrows(Exception.class, () -> {facade.create(null, "nope");});
    }

    @Test
    public void joinPass() throws Exception {
        String[] args = new String[]{"username6", "password6", "email6"};
        AuthData authData = facade.register(args);
        facade.logout(authData);
        AuthData auth = facade.login(new String[]{"username6", "password6"});
        GameData game = facade.create(auth, "gameName6");
        HashMap<Integer, GameData> chessGames = new HashMap<>();
        chessGames.put(1, game);
        facade.join(auth, new String[]{"1", "WHITE"}, chessGames);
        assertNotNull(chessGames.get(1));//.whiteUsername());
    }

    @Test
    public void joinFail() {
        assertThrows(Exception.class, () -> {facade.join(null, new String[]{"no"}, null);});
    }
}
