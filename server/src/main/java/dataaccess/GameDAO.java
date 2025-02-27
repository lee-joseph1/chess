package dataaccess;

import model.GameData;

public interface GameDAO {
    void createGame(GameData gameData);
    void clear();
    GameData getGameByID(Integer gameID);
    void updateGame(Integer gameID, GameData gameData);
    //HashMap getAllGames
    //maybe use a collection here instead?? nice to organize by game id but idk ill think ab it
}
