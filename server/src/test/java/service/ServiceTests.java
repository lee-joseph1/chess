package service;

import dataaccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.requests.*;
import service.responses.*;
import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {
    private AuthDAO authDao;
    private UserDAO userDao;
    private GameDAO gameDao;
    private AuthService authService;
    private UserService userService;
    private GameService gameService;

    @BeforeEach
    public void setUp() {
        authDao = new MemoryAuthDAO();
        userDao = new MemoryUserDAO();
        gameDao = new MemoryGameDAO();
        authService = new AuthService(authDao, userDao);
        userService = new UserService(authDao, userDao);
        gameService = new GameService(authDao, gameDao);
    }

    @Test
    public void passRegister() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("user", "password", "e@mail.mail");
        RegisterResponse registerResponse = userService.register(registerRequest);
        assertNotNull(registerResponse);
        assertNotNull(registerResponse.authToken());
        assertEquals("user", registerResponse.username());
    }

    @Test
    public void failRegister() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("user", "password", "e@mail.mail");
        userService.register(registerRequest);
        RegisterRequest register2Request = new RegisterRequest("user", "p2", "e@mail.mail");
        assertThrows(DataAccessException.class, () -> userService.register(register2Request));
    }

    @Test
    public void clearUser() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("user", "password", "e@mail.mail");
        userService.register(registerRequest);
        userService.clear();
        assertNull(userDao.getUserByUsername("user"));
    }

    @Test
    public void passLogin() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("user", "password", "e@mail.mail");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("user", "password");
        LoginResponse loginResponse = authService.login(loginRequest);
        assertNotNull(loginResponse);
        assertNotNull(loginResponse.authToken());
        assertNotNull(authDao.getAuthByToken(loginResponse.authToken()));
        assertEquals("user", loginResponse.username());
    }

    @Test
    public void failLogin() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("user", "password", "e@mail.mail");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("user", "wrong");
        assertThrows(DataAccessException.class, () -> authService.login(loginRequest));
    }

    @Test
    public void passLogout() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("user", "password", "e@mail.mail");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("user", "password");
        LoginResponse loginResponse = authService.login(loginRequest);
        LogoutRequest logoutRequest = new LogoutRequest(loginResponse.authToken());
        LogoutResponse logoutResponse = authService.logout(logoutRequest);
        assertNotNull(logoutResponse);
        assertNull(authDao.getAuthByToken(loginResponse.authToken()));
    }

    @Test
    public void failLogout() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("user", "password", "e@mail.mail");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("user", "password");
        authService.login(loginRequest);
        LogoutRequest logoutRequest = new LogoutRequest("wrong");
        assertThrows(DataAccessException.class, () -> authService.logout(logoutRequest));
    }

    @Test
    public void clearAuth() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("user", "password", "e@mail.mail");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("user", "password");
        LoginResponse loginResponse = authService.login(loginRequest);
        authService.clear();
        assertNull(authDao.getAuthByToken(loginResponse.authToken()));
    }

    @Test
    public void passCreate() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("user", "password", "e@mail.mail");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("user", "password");
        LoginResponse loginResponse = authService.login(loginRequest);
        CreateRequest createRequest = new CreateRequest("name");
        CreateResponse createResponse = gameService.create(createRequest, loginResponse.authToken());
        assertNotNull(createRequest);
        assertEquals("name", gameDao.getGameByID(createResponse.gameID()).gameName());
    }

    @Test
    public void failCreate() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("user", "password", "e@mail.mail");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("user", "password");
        authService.login(loginRequest);
        CreateRequest createRequest = new CreateRequest("name");
        assertThrows(DataAccessException.class, () -> gameService.create(createRequest, "wrong"));
    }

    @Test
    public void passJoin() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("user", "password", "e@mail.mail");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("user", "password");
        LoginResponse loginResponse = authService.login(loginRequest);
        CreateRequest createRequest = new CreateRequest("name");
        CreateResponse createResponse = gameService.create(createRequest, loginResponse.authToken());
        assertNotNull(createRequest);
        assertEquals("name", gameDao.getGameByID(createResponse.gameID()).gameName());
    }

    @Test
    public void failJoin() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("user", "password", "e@mail.mail");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("user", "password");
        LoginResponse loginResponse = authService.login(loginRequest);
        CreateRequest createRequest = new CreateRequest("name");
        CreateResponse createResponse = gameService.create(createRequest, loginResponse.authToken());
        JoinRequest joinRequest = new JoinRequest("WHITE", createResponse.gameID());
        JoinResponse joinResponse = gameService.join(joinRequest, loginResponse.authToken());
        assertNotNull(joinResponse);
        assertEquals("user", gameDao.getGameByID(createResponse.gameID()).whiteUsername());
    }

    @Test
    public void passList() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("user", "password", "e@mail.mail");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("user", "password");
        LoginResponse loginResponse = authService.login(loginRequest);
        CreateRequest createRequest = new CreateRequest("name");
        gameService.create(createRequest, loginResponse.authToken());
        ListResponse listResponse = gameService.list(loginResponse.authToken());
        assertNotNull(listResponse);
        assertFalse(listResponse.games().isEmpty());
    }

    @Test
    public void failList() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("user", "password", "e@mail.mail");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("user", "password");
        LoginResponse loginResponse = authService.login(loginRequest);
        CreateRequest createRequest = new CreateRequest("name");
        gameService.create(createRequest, loginResponse.authToken());
        assertThrows(DataAccessException.class, () -> gameService.list("wrong"));
    }

    @Test
    public void clearGame() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("user", "password", "e@mail.mail");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("user", "password");
        LoginResponse loginResponse = authService.login(loginRequest);
        CreateRequest createRequest = new CreateRequest("name");
        CreateResponse createResponse = gameService.create(createRequest, loginResponse.authToken());
        ListResponse listResponse = gameService.list(loginResponse.authToken());
        assertFalse(listResponse.games().isEmpty());
        gameService.clear();
        assertNull(gameDao.getGameByID(createResponse.gameID()));
    }
}

