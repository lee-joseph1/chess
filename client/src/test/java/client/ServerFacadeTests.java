package client;

import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import model.AuthData;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    public static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void RegisterPass() throws Exception {
        String[] args = new String[]{"username", "password", "email"};
        AuthData authData = facade.register(args);
        assertNotNull(authData.authToken());
    }

    @Test
    public void registerFail() {
        Exception exception = assertThrows(Exception.class, () -> {facade.register(new String[]{null, "password", "email"});});
        assertEquals("Error: bad request", exception.getMessage());

    }

//    @Test
//    public void loginSuccess() throws IOException {
//        AuthData authData = facade.login("user", "password");
//        assertNotNull(authData.authToken());
//    }
//
//    @Test
//    public void loginFailure() {
//        Exception exception = assertThrows(ResponseException.class, () -> {facade.login(null, null);});
//        assertEquals("Error: unauthorized", exception.getMessage());
//    }

}
