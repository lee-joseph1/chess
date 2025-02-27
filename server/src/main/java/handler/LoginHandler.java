package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.UserService;
import service.requests.LoginRequest;
import service.responses.LoginResponse;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler implements Route {
    private final UserService userService;
    private final Gson gson = new Gson();

    public LoginHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        try {
            LoginRequest loginRequest = gson.fromJson(request.body(), LoginRequest.class);
            LoginResponse loginResponse = userService.login(loginRequest);
            response.status(200);
            response.body("{\"username\":\"\",\"authToken\":\"");
            return gson.toJson(loginResponse);
        } catch (IllegalArgumentException exception) {
            response.status(400);
            return "{\"message\":\"Error: bad request\"}";
        }
        catch (DataAccessException exception) {
            response.status(401);
            return "{\"message\":\"Error: unauthorized\"}";
        }
        catch (Exception exception) {
            response.status(500);
            return "{\"message\":\"Error: " + exception.getMessage() + "\"}";
        }
    }
}
