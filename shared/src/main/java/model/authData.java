package model;

public record authData(String authToken, String username) {
    @Override
    public String authToken() {
        return authToken;
    }

    @Override
    public String username() {
        return username;
    }
}