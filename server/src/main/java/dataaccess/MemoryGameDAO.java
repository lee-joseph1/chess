package dataaccess;

import model.GameData;
import java.util.ArrayList;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    HashMap<Integer, GameData> gameMap = new HashMap<>();
    @Override
    public int createGame(GameData gameData) {
        gameMap.put(gameData.gameID(), gameData);
        return gameData.gameID();
    }

    @Override
    public GameData getGameByID(Integer gameID) {
        return gameMap.get(gameID);
    }

    @Override
    public void updateGame(Integer gameID, GameData gameData) {
        gameMap.replace(gameID, gameData);
    }

    @Override
    public ArrayList<GameData> getAllGames() {
        return new ArrayList<>(gameMap.values());
    }

    @Override
    public void clear() {
        gameMap.clear();
    }
}
