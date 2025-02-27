package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import service.requests.LoginRequest;
import service.responses.LoginResponse;

public class AuthService {
    private final AuthDAO authDao;
    private final GameDAO gameDao;
    private final UserDAO userDao;

    public AuthService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO) {
        this.authDao = authDAO;
        this.userDao = userDAO;
        this.gameDao = gameDAO;
    }

    public void login(String username, String password) {}

    public void clear() {
        authDao.clear();
    }

    public LoginResponse login(LoginRequest request) {
        return null;
        //if (request.username() == null || request.password() == null || request.email == null) {}
    }
}
