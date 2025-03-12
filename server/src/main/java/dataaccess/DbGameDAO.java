package dataaccess;

import com.google.gson.Gson;
import model.GameData;
import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static java.sql.Types.NULL;

public class DbGameDAO implements GameDAO{
    public DbGameDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void createGame(GameData gameData) {
        var stmt = "INSERT INTO gameData (gameName, whiteUsername, blackUsername, json) VALUES (?,?,?,?)";
        var json = new Gson().toJson(gameData);
        String whiteUsername = gameData.whiteUsername();
        if (whiteUsername == null) {
            whiteUsername = "";
        }
        String blackUsername = gameData.blackUsername();
        if (blackUsername == null) {
            blackUsername = "";
        }
        try {
            executeUpdate(stmt, gameData.gameName(), whiteUsername, blackUsername, json);
        }
        catch (Exception ex) {
            throw new RuntimeException("Error creating game: " + ex.getMessage());
        }
    }

    @Override
    public void clear() {
        var stmt = "TRUNCATE TABLE gameData";
        try {
            executeUpdate(stmt);
        }
        catch (Exception ex) {
            throw new RuntimeException("Error clearing game: " + ex.getMessage());
        }
    }

    @Override
    public GameData getGameByID(Integer gameID) {
        try (var conn = DatabaseManager.getConnection()) {
            var stmt = "SELECT * FROM userData WHERE gameID = ?";
            try (var ps = conn.prepareStatement(stmt)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Gson().fromJson(rs.getString("json"), GameData.class);
                    }
                }
            }
        } catch (DataAccessException | SQLException ex) {
            throw new RuntimeException("Error getting user by username: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public void updateGame(Integer gameID, GameData gameData) {
        var stmt = "UPDATE gameData SET gameName = ?, whiteUsername = ?, " +
                "blackUsername = ?, json = ? WHERE gameID = ?";
        var json = new Gson().toJson(gameData);
        String whiteUsername = gameData.whiteUsername();
        if (whiteUsername == null) {
            whiteUsername = "";
        }
        String blackUsername = gameData.blackUsername();
        if (blackUsername == null) {
            blackUsername = "";
        }
        try {
            executeUpdate(stmt, gameData.gameName(), whiteUsername, blackUsername, json, gameID);
        }
        catch (Exception ex){
            throw new RuntimeException("Error updating game: " + ex.getMessage());
        }
    }

    @Override
    public ArrayList<GameData> getAllGames() {
        ArrayList<GameData> games = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            var stmt = "SELECT * FROM gameData ORDER BY gameID";
            try (var ps = conn.prepareStatement(stmt)) {
                try (var rs = ps.executeQuery()) {
                    var gameData = new Gson().fromJson(rs.getString("json"), GameData.class);
                    games.add(gameData);
                }
            }
            return games;
        }
        catch (DataAccessException | SQLException ex) {
            throw new RuntimeException("Error getting all games: " + ex.getMessage());
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS gameData (
            'id' int NOT NULL AUTO_INCREMENT,
            'gameName' varchar(256) NOT NULL,
            'whiteUsername' VARCHAR(256),
            'blackUsername' VARCHAR(256),
            'json' TEXT DEFAULT NULL,
            PRIMARY KEY('id'))
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var stmt : createStatements) {
                try (var ps = conn.prepareStatement(stmt)) {
                    ps.executeUpdate();
                }
            }
        } catch (DataAccessException | SQLException ex) {
            throw new RuntimeException("Error creating database");
        }
    }

    private void executeUpdate(String stmt, Object... params) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(stmt)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();
            }
        } catch (DataAccessException | SQLException ex) {
            throw new RuntimeException("Failed to update database");
        }
    }
}
