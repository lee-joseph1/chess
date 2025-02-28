//package service;
//
//import chess.ChessGame;
//import dataaccess.AuthDAO;
//import dataaccess.DataAccessException;
//import dataaccess.GameDAO;
//import dataaccess.UserDAO;
//import model.GameData;
//import model.UserData;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import service.requests.ListRequest;
//import service.responses.ListResponse;
//
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class GameServiceTests {
//    private AuthDAO authDao;
//    private GameDAO gameDao;
//    private UserDAO userDao;
//    private GameService gameService;
//
//    public GameServiceTests(AuthDAO authDao, GameDAO gameDao, UserDAO userDao) {
//        this.authDao = authDao;
//        this.gameDao = gameDao;
//        this.userDao = userDao;
//    }
//
//    @BeforeEach
//    void setup() throws DataAccessException {
//        gameService = new GameService(authDao, userDao, gameDao);
//
//        userDao.createUser(new UserData("User", "Password", "email@byu.edu"));
//        authDao.createAuth(new model.AuthData("auth-token", "User"));
//    }
//
////    @Test
////    void testListGamesSuccess() throws DataAccessException {
////        gameDao.createGame(new GameData(1002, null, null, "Game1", new ChessGame()));
////        gameDao.createGame(new GameData(1003, "User", null, "Game2", new ChessGame()));
////        ListResponse response = gameService.list(new ListRequest("auth-token"));
////        assertNotNull(response);
////        ArrayList<GameData> games = response.response();
////        assertEquals(2, games.size());
//    }
//
//    @Test
//    void testListGamesUnauthorized() {
//        assertThrows (DataAccessException.class, () -> gameService.list(new ListRequest("false-token")),
//                "accepted invalid authToken");
//    }
//
//}
