package dataaccess;

import model.authData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    private final HashMap<String, authData> authMap = new HashMap<>();
    @Override
    public void createAuth(authData authData) {
        authMap.put(authData.username(), authData);
    }

    @Override
    public void clear() {
        authMap.clear();
    }
}
