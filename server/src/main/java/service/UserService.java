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

    public UserService(AuthDAO authDao, UserDAO userDao) {
        this.authDao = authDao;
        this.userDao = userDao;
    }

    public RegisterResponse register(RegisterRequest request) throws DataAccessException {
        if (userDao.getUserByUsername(request.username()) != null) {
            throw new DataAccessException("already taken");
        }
        if (request.username() == null || request.password() == null || request.email() == null) {
            throw new IllegalArgumentException("bad request");
        }
        userDao.createUser(new UserData(request.username(), request.password(), request.email()));
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, request.username());
        authDao.createAuth(authData);
        return new RegisterResponse(request.username(), authToken);
    }

    public void clear() {
        userDao.clear();
    }
}