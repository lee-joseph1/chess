package dataaccess;

public interface GameDAO {
    void createGame(String gameID, String whiteUsername, String blackUsername, String gameName);
    void clear();
    //game getGameByID(String gameID);
    //void updateGame(String gameID, String playerColor, String username);
    //HashMap getAllGames
    //maybe use a collection here instead?? nice to organize by game id but idk ill think ab it
}
