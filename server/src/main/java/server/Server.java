package server;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.UserDAO;
import dataaccess.MemoryUserDAO;
import handler.*;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;


public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        UserDAO userDao = new MemoryUserDAO();
        AuthDAO authDao = new MemoryAuthDAO();
        GameDAO gameDao = new MemoryGameDAO();
        UserService userService = new UserService(authDao, userDao);
        AuthService authService = new AuthService(authDao, userDao);
        GameService gameService = new GameService(authDao, gameDao);
        //register user
        Spark.post("/user", new RegisterHandler(userService));
        //login
        Spark.post("/session", new LoginHandler(authService));
        //logout
        Spark.delete("/session", new LogoutHandler(authService));
        //create game
        Spark.post("/game", new CreateHandler(gameService));
        //join game
        Spark.put("/game", new JoinHandler(gameService));
        //list games
        Spark.get("/game", new ListHandler(gameService));
        //clear
        Spark.delete("/db", new ClearHandler(authService, userService, gameService));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
