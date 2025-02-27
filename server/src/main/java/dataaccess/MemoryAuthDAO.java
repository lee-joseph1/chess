package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    private final HashMap<String, AuthData> authMap = new HashMap<>();
    private final HashMap<String, AuthData> tokenMap = new HashMap<>();
    @Override
    public void createAuth(AuthData authData) {
        authMap.put(authData.username(), authData);
        tokenMap.put(authData.authToken(), authData);
    }

    @Override
    public AuthData getAuthByUser(String username) {
        return authMap.get(username);
    }

    @Override
    public AuthData getAuthByToken(String authToken) {
        return tokenMap.get(authToken);
    }

    @Override
    public HashMap<String, AuthData> getAllAuths() {
        return authMap;
    }

    @Override
    public void deleteAuth(AuthData authData) {
        authMap.remove(authData.username());
        tokenMap.remove(authData.authToken());
    }

    @Override
    public void clear() {
        authMap.clear();
    }
}
