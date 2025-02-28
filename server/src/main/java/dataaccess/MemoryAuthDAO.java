package dataaccess;

import model.AuthData;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private final HashMap<String, AuthData> authMap = new HashMap<>();
    private final HashSet<String> tokens = new HashSet<>();

    @Override
    public void createAuth(AuthData authData) {
        authMap.put(authData.authToken(), authData);
    }

    @Override
    public String generateUniqueToken() {
        String token = UUID.randomUUID().toString();
        while (tokens.contains(token)) {
            token = UUID.randomUUID().toString();
        }
        tokens.add(token);
        return token;
        }

    @Override
    public AuthData getAuthByToken(String authToken) {
        return authMap.get(authToken);
    }

    @Override
    public void deleteAuth(AuthData authData) {
        authMap.remove(authData.authToken());
    }

    @Override
    public void clear() {
        authMap.clear();
        tokens.clear();
    }
}
