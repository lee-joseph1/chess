package dataaccess;

import model.AuthData;
import java.util.HashMap;

public interface AuthDAO {
    void createAuth(AuthData authData);
    void clear();
    AuthData getAuthByUser(String username);
    HashMap<String, AuthData> getAllAuths();
}
