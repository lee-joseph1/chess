package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import service.requests.CreateRequest;
import service.requests.JoinRequest;
import service.responses.CreateResponse;
import service.responses.JoinResponse;
import service.responses.ListResponse;
import java.util.Arrays;


public class GameService {
    private final AuthDAO authDao;
    private final GameDAO gameDao;
    private int currentGameID = 1001;

    public GameService(AuthDAO authDao, GameDAO gameDao) {
        this.authDao = authDao;
        this.gameDao = gameDao;
    }

    public CreateResponse create(CreateRequest request, String authToken) throws DataAccessException {
        if (request.gameName() == null) {//about to give ID, players not required to create
            throw new IllegalArgumentException("bad request");
        }
        if (noAuth(authToken)) {
            throw new DataAccessException("unauthorized");
        }
        GameData gameData = new GameData(0, null, null, request.gameName(), new ChessGame());
        int gameID = gameDao.createGame(gameData);
        return new CreateResponse(gameID);
    }

    public JoinResponse join(JoinRequest request, String authToken) throws Exception {
        if (request.gameID() <= 1000 || request.playerColor() == null) {
            throw new IllegalArgumentException("bad request");
        }
        GameData gameData = gameDao.getGameByID(request.gameID());
        String playerColor = request.playerColor().toUpperCase();
        if ((playerColor.equals("WHITE") && gameData.whiteUsername() != null) ||
                (playerColor.equals("BLACK") && gameData.blackUsername() != null)) {
            throw new IllegalArgumentException("already taken");
        }
        if (noAuth(authToken)) {
            throw new DataAccessException("unauthorized");
        }
        if (!Arrays.asList("WHITE","BLACK").contains(playerColor)) {
            throw new IllegalArgumentException("bad request");
        }
        String username = authDao.getAuthByToken(authToken).username();
        if (gameData == null) {
            throw new DataAccessException("game does not exist");
        }
        if (playerColor.equals("WHITE")) {
            gameData = new GameData(gameData.gameID(), username, gameData.blackUsername(),
                    gameData.gameName(), gameData.game());
        }
        else {
            gameData = new GameData(gameData.gameID(), gameData.whiteUsername(), username,
                    gameData.gameName(), gameData.game());
        }
        gameDao.updateGame(gameData.gameID(), gameData);
        return new JoinResponse();
    }

    public ListResponse list(String request) throws DataAccessException {
        if (noAuth(request)) {
            throw new DataAccessException("unauthorized");
        }
        return new ListResponse(gameDao.getAllGames());
    }

    private boolean noAuth(String authToken) {
        return authToken == null || authDao.getAuthByToken(authToken) == null;
    }

    public void clear() {
        gameDao.clear();
    }
}