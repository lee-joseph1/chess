package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import service.requests.LoginRequest;
import service.requests.LogoutRequest;
import service.responses.LoginResponse;
import service.responses.LogoutResponse;

public class AuthService {
    private final AuthDAO authDao;
    private final GameDAO gameDao;
    private final UserDAO userDao;

    public AuthService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO) {
        this.authDao = authDAO;
        this.userDao = userDAO;
        this.gameDao = gameDAO;
    }

    public LogoutResponse logout(LogoutRequest request) throws DataAccessException {
        AuthData authData = authDao.getAuthByToken(request.authToken)
    }

    public void clear() {
        authDao.clear();
    }

    public LoginResponse login(LoginRequest request) {
        return null;
        //if (request.username() == null || request.password() == null || request.email == null) {}
    }
}
