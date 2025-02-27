package dataaccess;

import model.GameData;

public interface GameDAO {
    void createGame(GameData gameData);
    void clear();
    //game getGameByID(String gameID);
    //void updateGame(String gameID, String playerColor, String username);
    //HashMap getAllGames
    //maybe use a collection here instead?? nice to organize by game id but idk ill think ab it
}
