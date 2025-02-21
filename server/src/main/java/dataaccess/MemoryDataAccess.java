package dataaccess;

import model.UserData;
import model.*;

import java.util.HashMap;
import java.util.Map;

public class MemoryDataAccess implements DataAccess {
    private Map<String, UserData> users = new HashMap<>();
    private Map<AuthData, String> authTokens = new HashMap<>();
    @Override
    public void createUser(UserData user) throws DataAccessException {
        if (users.containsKey(user.username())) {
            throw new DataAccessException("Username already exists");
        }
        users.put(user.username(), user);
    }
    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        authTokens.put(auth, auth.username());
    }
}
