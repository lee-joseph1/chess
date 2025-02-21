package dataaccess;

//import model.AuthData;
import java.util.Collection;

public interface AuthDAO {
    void createAuth(String username, String authToken) throws Exception;
    void deleteAuth(String username, String authToken) throws Exception;
    //AuthData getAuthByUser(String username);
    //AuthData getAuthByToken(String authToken);
    void clearAuth();
    //Collection<AuthData> getAllAuth();
}
