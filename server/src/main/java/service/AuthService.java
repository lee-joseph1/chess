package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import service.requests.LoginRequest;
import service.requests.LogoutRequest;
import service.responses.LoginResponse;
import service.responses.LogoutResponse;

import java.util.UUID;

public class AuthService {
    private final AuthDAO authDao;
    private final GameDAO gameDao;
    private final UserDAO userDao;

    public AuthService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO) {
        this.authDao = authDAO;
        this.userDao = userDAO;
        this.gameDao = gameDAO;
    }

    public LoginResponse login(LoginRequest request) throws DataAccessException {
        if (request.username() == null || request.password() == null) {
            throw new IllegalArgumentException("bad request");
        }
        UserData user = userDao.getUserByUsername(request.username());
        if (user == null || !(user.password().equals(request.password()))) {
            throw new DataAccessException("Username/Password is incorrect");
        }
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, request.username());
        authDao.createAuth(authData);
        return new LoginResponse(request.username(), authToken);
    }

    public LogoutResponse logout(LogoutRequest request) throws DataAccessException {
        if (request.authToken() == null) {
            throw new IllegalArgumentException("bad request");
        }
        AuthData authData = authDao.getAuthByToken(request.authToken());
        if (authData == null) {
            throw new DataAccessException("unauthorized auth");
        }
        authDao.deleteAuth(authData);
        return new LogoutResponse();
    }

    public void clear() {
        authDao.clear();
    }
}
