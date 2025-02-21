package server;

import service.UserService;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import handler.RegHandler;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        DataAccess dao = new MemoryDataAccess();
        UserService userService = new UserService(dao);
        //register user
        Spark.post("/user", new RegHandler(userService));

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
