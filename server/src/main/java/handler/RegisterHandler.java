package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.UserService;
import service.requests.RegisterRequest;
import service.responses.RegisterResponse;
import spark.Route;
import spark.Request;
import spark.Response;

public class RegisterHandler implements Route {
    private final UserService userService;
    private final Gson gson = new Gson();

    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }

    public Object handle(Request request, Response response) {
        try {
            RegisterRequest registerRequest = gson.fromJson(request.body(), RegisterRequest.class);
            RegisterResponse registerResponse = userService.register(registerRequest);
            return gson.toJson(registerResponse);
        }
        catch (IllegalArgumentException exception) {
            response.status(400);
            return "{\"message\":\"Error: bad request\"}";
        }
        catch (DataAccessException exception) {
            if (exception.getMessage().contains("User already exists")
                    || exception.getMessage().contains("already taken")) {
                response.status(403);
                return "{\"message\":\"Error: already taken\"}";
            }
        }
        catch (Exception exception) {
            response.status(500);
            return "{\"message\":\"Error: " + exception.getMessage() + "\"}";
        }
        response.status(200);
        return "{\"username\":\"\", \"authToken\":\"\"}";
    }
}
