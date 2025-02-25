package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.UserData;
import model.AuthData;
import service.requests.RegisterRequest;
import service.responses.RegisterResponse;

import java.util.UUID;


public class UserService {
    private final AuthDAO authDao;
    private final UserDAO userDao;
    private final GameDAO gameDao;
    //    public RegisterResult register(RegisterRequest registerRequest) {}
//    public LoginResult login(LoginRequest loginRequest) {}
//    public void logout(LogoutRequest logoutRequest) {}

    public UserService(AuthDAO authDao, UserDAO userDao, GameDAO gameDao) {
        this.authDao = authDao;
        this.userDao = userDao;
        this.gameDao = gameDao;
    }

    public RegisterResponse register(RegisterRequest request) throws DataAccessException {
        if (userDao.getUserByUsername(request.username()) != null) {
            throw new DataAccessException("already taken");
        }
        if (request.username() == null || request.email() == null || request.password() == null) {
            throw new IllegalArgumentException("bad request");
        }
        UserData user = new UserData(request.username(), request.password(), request.email());
        userDao.createUser(user);
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, request.username());
        authDao.createAuth(authData);
        return new RegisterResponse(request.username(), authToken);
    }

    public void clear() {
        userDao.clear();
        authDao.clear();
    }
}