package dataaccess;

import model.AuthData;

public class DbAuthDAO implements AuthDAO {

    @Override
    public void createAuth(AuthData authData) {

    }

    @Override
    public void clear() {

    }

    @Override
    public AuthData getAuthByToken(String authToken) {
        return null;
    }

    @Override
    public void deleteAuth(AuthData authData) {

    }

    @Override
    public String generateUniqueToken() {
        return "";
    }
}
