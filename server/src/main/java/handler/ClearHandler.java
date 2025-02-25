package handler;

import com.google.gson.Gson;
import service.UserService;
import spark.Route;
import spark.Request;
import spark.Response;

public class ClearHandler implements Route {
    private final UserService userService;

    public ClearHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Object handle(Request request, Response response) {
        try {
                userService.clear();
                //gameService.clear();
                //authService.clear();
            } catch (Exception exception) {
                response.status(500);
                return "{\"message\":\"Error: " + exception.getMessage() + "\"}";
            }
            response.status(200);
            return "{}";
    }
}