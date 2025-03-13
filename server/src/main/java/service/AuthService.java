package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import service.requests.LoginRequest;
import service.requests.LogoutRequest;
import service.responses.LoginResponse;
import service.responses.LogoutResponse;

public class AuthService {
    private final AuthDAO authDao;
    private final UserDAO userDao;

    public AuthService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDao = authDAO;
        this.userDao = userDAO;
    }

    public LoginResponse login(LoginRequest request) throws DataAccessException {
        if (request.username() == null || request.password() == null) {
            throw new IllegalArgumentException("bad request");
        }
        UserData user = userDao.getUserByUsername(request.username());
        if (user == null || !userDao.verifyUser(request.username(), request.password())) {//!(user.password().equals(request.password()))) {
            throw new DataAccessException("Username/Password is incorrect");
        }
        String authToken = authDao.generateUniqueToken();
        AuthData authData = new AuthData(authToken, request.username());
        authDao.createAuth(authData);
        return new LoginResponse(request.username(), authToken);
    }

//    boolean verifyUser(String username, String providedClearTextPassword) {
//        UserData userData = userDao.getUserByUsername(username);
//        if (userData != null) {
//            String hashedPassword = userData.password();
//            return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
//        }
//        return false;
//    }

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
