package service.requests;

public record JoinRequest(String playerColor, int gameID) {
    @Override
    public String playerColor() {
        return playerColor;
    }

    @Override
    public int gameID() {
        return gameID;
    }
}
