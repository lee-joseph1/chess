package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.GameService;
import service.responses.ListResponse;
import spark.Request;
import spark.Response;
import spark.Route;

public class ListHandler implements Route {
    private final GameService gameService;
    private final Gson serializer = new Gson();

    public ListHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public Object handle(Request request, Response response) {
        try {
            ListResponse listResponse = gameService.list(request.headers("authorization"));
            response.status(200);
            return serializer.toJson(listResponse);
        }
        catch (DataAccessException exception) {
            response.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
        }
        catch (Exception exception) {
            response.status(500);
            return "{\"message\":\"Error: " + exception.getMessage() + "\"}";
        }
    }
}
