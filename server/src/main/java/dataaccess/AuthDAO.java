package dataaccess;

import model.AuthData;
import java.util.HashMap;

public interface AuthDAO {
    void createAuth(AuthData authData);
    void clear();
    AuthData getAuthByUser(String username);
    AuthData getAuthByToken(String authToken);
    HashMap<String, AuthData> getAllAuths();
    void deleteAuth(AuthData authData);
}
