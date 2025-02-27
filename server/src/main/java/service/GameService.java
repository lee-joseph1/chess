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
import service.requests.RegisterRequest;
import service.responses.CreateResponse;
import service.responses.RegisterResponse;

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

//    public ListResponse list(ListRequest request) throws DataAccessException {
//
//    }
//
//    public JoinResponse join(JoinRequest request) throws DataAccessException {
//
//    }

    public boolean checkAuth(String authToken) {
        return authToken != null && authDao.getAuthByToken(authToken) != null;
    }

    public void clear() {
        gameDao.clear();
    }
}