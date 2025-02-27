package dataaccess;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    HashMap gameMap = new HashMap();
    @Override
    public void createGame(String gameID, String whiteUsername, String blackUsername, String gameName) {

    }

    @Override
    public void clear() {
        gameMap.clear();
    }
}
