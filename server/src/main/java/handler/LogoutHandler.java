package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.AuthService;
import service.requests.LogoutRequest;
import service.responses.LogoutResponse;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route {
    private final AuthService authService;
    private final Gson serializer = new Gson();

    public LogoutHandler(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Object handle(Request request, Response response) {
        try {
            LogoutRequest logoutRequest = new LogoutRequest(request.headers("authorization"));
            LogoutResponse logoutResponse = authService.logout(logoutRequest);
            response.status(200);
            return serializer.toJson(logoutResponse);
        }
        catch (DataAccessException exception) {
            response.status(401);
            return "{\"message\":\"Error: unauthorized\"}";
        }
        catch (Exception exception)  {
            response.status(500);
            return "{\"message\":\"Error: " + exception.getMessage() + "\"}";
        }
    }
}
