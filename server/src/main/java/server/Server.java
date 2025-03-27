package server;

import dataaccess.*;
import handler.*;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;


public class Server {

    public int run(int desiredPort) {
        try {
            Spark.port(desiredPort);

            Spark.staticFiles.location("web");

            // Register your endpoints and handle exceptions here.
            UserDAO userDao = new DbUserDAO();//change these for memory/db
            AuthDAO authDao = new DbAuthDAO();
            GameDAO gameDao = new DbGameDAO();
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
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return desiredPort;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
