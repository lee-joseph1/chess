package service.responses;

public record RegisterResponse (String username, String authToken) {
    @Override
    public String username() {
        return username;
    }

    @Override
    public String authToken() {
        return authToken;
    }
}
