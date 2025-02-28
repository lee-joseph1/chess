package service.requests;

public record ListRequest(String authToken) {
    @Override
    public String authToken() {
        return authToken;
    }
}
