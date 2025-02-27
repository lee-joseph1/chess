package service.requests;

public record LogoutRequest (String authToken){
    @Override
    public String authToken() {
        return authToken;
    }
}
