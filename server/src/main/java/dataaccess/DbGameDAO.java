package dataaccess;

import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;

public class DbGameDAO implements GameDAO{
    public DbGameDAO() throws SQLException {
        String createAuthTable = """
            CREATE TABLE IF NOT EXISTS gameData (
            'id' int NOT NULL AUTO_INCREMENT,
            'whiteUsername' VARCHAR(256),
            'blackUsername' VARCHAR(256),
            'gameName' varchar(256) NOT NULL,
            'json' TEXT DEFAULT NULL,
            PRIMARY KEY('id'))""";
    }

    @Override
    public void createGame(GameData gameData) {

    }

    @Override
    public void clear() {

    }

    @Override
    public GameData getGameByID(Integer gameID) {
        return null;
    }

    @Override
    public void updateGame(Integer gameID, GameData gameData) {

    }

    @Override
    public ArrayList<GameData> getAllGames() {
        return null;
    }
}
