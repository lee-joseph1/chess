package dataaccess;

import model.GameData;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    HashMap<Integer, GameData> gameMap = new HashMap<>();
    @Override
    public void createGame(GameData gameData) {
        gameMap.put(gameData.gameID(), gameData);
    }

    @Override
    public void clear() {
        gameMap.clear();
    }
}
