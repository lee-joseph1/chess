package handler;

import com.google.gson.Gson;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.Route;
import spark.Request;
import spark.Response;

public class ClearHandler implements Route {
    private final UserService userService;
    private final GameService gameService;
    private final AuthService authService;

    public ClearHandler(AuthService authService, UserService userService, GameService gameService) {
        this.authService = authService;
        this.userService = userService;
        this.gameService = gameService;
    }

    @Override
    public Object handle(Request request, Response response) {
        try {
                userService.clear();
                gameService.clear();
                authService.clear();
            } catch (Exception exception) {
                response.status(500);
                return "{\"message\":\"Error: " + exception.getMessage() + "\"}";
            }
            response.status(200);
            return "{}";
    }
}