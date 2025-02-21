package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.UserData;
import model.AuthData;
import service.request.RegRequest;
import service.response.RegResponse;

import java.util.UUID;


public class UserService {
//    public RegisterResult register(RegisterRequest registerRequest) {}
//    public LoginResult login(LoginRequest loginRequest) {}
//    public void logout(LogoutRequest logoutRequest) {}
    private DataAccess dao;
    public UserService(DataAccess dao) {
        this.dao = dao;
    }
    public RegResponse register(RegRequest request) throws DataAccessException {
        if (request.username() == null || request.password() == null || request.email() == null) {
            throw new IllegalArgumentException("Missing required fields");
        }
        UserData user = new UserData(request.username(), request.password(), request.email());
        dao.createUser(user);
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, request.username());
        dao.createAuth(authData);
        return new RegResponse(authToken, request.username());
    }
}