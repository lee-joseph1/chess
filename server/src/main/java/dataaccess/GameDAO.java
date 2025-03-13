package dataaccess;

import model.GameData;
import java.util.ArrayList;

public interface GameDAO {
    int createGame(GameData gameData);
    void clear();
    GameData getGameByID(Integer gameID);
    void updateGame(Integer gameID, GameData gameData);
    ArrayList<GameData> getAllGames();
}
