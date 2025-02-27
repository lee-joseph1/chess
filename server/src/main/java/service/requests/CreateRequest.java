package service.requests;

public record CreateRequest(String gameName) {
    @Override
    public String gameName() {
        return gameName;
    }
}
