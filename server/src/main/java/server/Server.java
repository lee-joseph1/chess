package server;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.UserDAO;
import dataaccess.MemoryUserDAO;
import handler.ClearHandler;
import handler.LoginHandler;
import handler.LogoutHandler;
import service.AuthService;
import service.GameService;
import service.UserService;
import handler.RegisterHandler;
import spark.*;


public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        UserDAO userDao = new MemoryUserDAO();
        AuthDAO authDao = new MemoryAuthDAO();
        GameDAO gameDao = new MemoryGameDAO();
        UserService userService = new UserService(authDao, userDao, gameDao);
        AuthService authService = new AuthService(authDao, userDao, gameDao);
        GameService gameService = new GameService(authDao, userDao, gameDao);
//        this.userService = new UserService(authDao, userDao, gameDao);
//        this.authService = new AuthService(authDao, userDao, gameDao);
//        this.gameService = new GameService(authDao, userDao, gameDao);
        //this.gameService = new GameService(userDao, authDao, gameDao);

        //register user
        Spark.post("/user", new RegisterHandler(userService));
        //login
        Spark.post("/session", new LoginHandler(authService));
        //logout
        Spark.delete("/session", new LogoutHandler(authService));
        //create game
        //a
        //list games
        //a
        //join game
        //a
        //clear (ish? required for register to pass, will revisit in greater detail)
        Spark.delete("/db", new ClearHandler(authService, userService, gameService));

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
