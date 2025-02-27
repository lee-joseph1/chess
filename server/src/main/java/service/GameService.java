package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.GameData;
import model.UserData;
import model.AuthData;
import service.requests.CreateRequest;
import service.requests.JoinRequest;
import service.requests.RegisterRequest;
import service.responses.CreateResponse;
import service.responses.JoinResponse;
import service.responses.RegisterResponse;

import java.util.Arrays;
import java.util.UUID;


public class GameService {
    private final AuthDAO authDao;
    private final UserDAO userDao;
    private final GameDAO gameDao;
    private int currentGameID = 1001;
    //    public RegisterResult register(RegisterRequest registerRequest) {}
//    public LoginResult login(LoginRequest loginRequest) {}
//    public void logout(LogoutRequest logoutRequest) {}

    public GameService(AuthDAO authDao, UserDAO userDao, GameDAO gameDao) {
        this.authDao = authDao;
        this.userDao = userDao;
        this.gameDao = gameDao;
    }

    public CreateResponse create(CreateRequest request, String authToken) throws DataAccessException {
        if (request.gameName() == null) {//about to give ID, players not required to create
            throw new IllegalArgumentException("bad request");
        }
        if (!checkAuth(authToken)) {//making it a new function cuz probably gonna come up again in list/join
            throw new DataAccessException("unauthorized");
        }
        int gameID = currentGameID;
        currentGameID++;
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(gameID, null, null, request.gameName(), game);
        gameDao.createGame(gameData);
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
        if (!checkAuth(authToken)) {
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

//    public ListResponse list(ListRequest request) throws DataAccessException {
//
//    }
//

    public boolean checkAuth(String authToken) {
        return authToken != null && authDao.getAuthByToken(authToken) != null;
    }

    public void clear() {
        gameDao.clear();
    }
}