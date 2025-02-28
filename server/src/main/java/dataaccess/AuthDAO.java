package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void createAuth(AuthData authData);
    void clear();
    AuthData getAuthByToken(String authToken);
    void deleteAuth(AuthData authData);
    String generateUniqueToken();
}
