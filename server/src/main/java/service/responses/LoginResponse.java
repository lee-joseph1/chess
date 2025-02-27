package service.responses;

public record LoginResponse (String username, String authToken) {
    @Override
    public String username() {
        return username;
    }

    @Override
    public String authToken() {
        return authToken;
    }
}
