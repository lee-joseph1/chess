package service.responses;

public record CreateResponse(int gameID){
    @Override
    public int gameID() {
        return gameID;
    }
}
