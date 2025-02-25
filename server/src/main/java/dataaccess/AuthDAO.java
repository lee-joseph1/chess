package dataaccess;

//import model.AuthData;
import model.AuthData;

public interface AuthDAO {
    void createAuth(AuthData authData);
    void clear();
    //void deleteAuth(String username, String authToken);
    //AuthData getAuthByUser(String username);
    //AuthData getAuthByToken(String authToken);
    //void clearAuth();
    //Collection<AuthData> getAllAuth();
}
