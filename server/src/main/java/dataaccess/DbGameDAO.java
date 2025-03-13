package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class DbGameDAO implements GameDAO{
    public DbGameDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public int createGame(GameData gameData) {
        var stmt = "INSERT INTO gameData (gameName, whiteUsername, blackUsername, json) VALUES (?,?,?,?)";
        var json = new Gson().toJson(gameData.game());
        String whiteUsername = gameData.whiteUsername();// == null ? "": gameData.whiteUsername();
        String blackUsername = gameData.blackUsername();// == null ? "": gameData.blackUsername();
        try {
            return executeUpdate(stmt, gameData.gameName(), whiteUsername, blackUsername, json);
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
            var stmt = "SELECT id, gameName, whiteUsername, blackUsername, json FROM gameData WHERE id = ?";
            try (var ps = conn.prepareStatement(stmt)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new GameData(gameID, rs.getString("whiteUsername"),
                                rs.getString("blackUsername"), rs.getString("gameName"),
                                new Gson().fromJson(rs.getString("json"), ChessGame.class));
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
        if (gameData == null) {
            throw new RuntimeException("game data cannot be null");
        }
        var stmt = "UPDATE gameData SET gameName = ?, whiteUsername = ?, " +
                "blackUsername = ?, json = ? WHERE id = ?";
        var json = new Gson().toJson(gameData.game());
        String whiteUsername = gameData.whiteUsername();// == null ? "": gameData.whiteUsername();
        String blackUsername = gameData.blackUsername();// == null ? "": gameData.blackUsername();
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
            var stmt = "SELECT * FROM gameData";
            try (var ps = conn.prepareStatement(stmt)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        var gameData = new GameData(rs.getInt("id"), rs.getString("whiteUsername"),
                                rs.getString("blackUsername"), rs.getString("gameName"),
                                new Gson().fromJson(rs.getString("json"), ChessGame.class));
                        games.add(gameData);
                    }
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
            `id` int NOT NULL AUTO_INCREMENT,
            `gameName` varchar(256) NOT NULL,
            `whiteUsername` VARCHAR(256),
            `blackUsername` VARCHAR(256),
            `json` TEXT DEFAULT NULL,
            PRIMARY KEY(`id`))
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (String stmt : createStatements) {
                try (var ps = conn.prepareStatement(stmt)) {
                    ps.executeUpdate();
                }
            }
        } catch (DataAccessException | SQLException ex) {
            throw new RuntimeException("Error creating game database");
        }
    }

    private int executeUpdate(String stmt, Object... params) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(stmt, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    var param = params[i];
                    switch (param) {
                        case String p -> ps.setString(i + 1, p);
                        case Integer p -> ps.setInt(i + 1, p);
                        case ChessGame p -> ps.setString(i + 1, new Gson().toJson(p));
                        case null -> ps.setNull(i + 1, NULL);
                        default -> {
                        }
                    }
                }
                ps.executeUpdate();
                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        } catch (DataAccessException | SQLException ex) {
            throw new RuntimeException("Failed to update database");
        }
    }
}
