package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.GameData;
import service.GameService;
import service.requests.CreateRequest;
import service.responses.CreateResponse;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateHandler implements Route {
    private final GameService gameService;
    private final Gson serializer = new Gson();

    public CreateHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        try {
            String authToken = request.headers("authorization");
            CreateRequest createRequest = serializer.fromJson(request.body(), CreateRequest.class);
            CreateResponse createResponse = gameService.create(createRequest, authToken);
            response.status(200);
            response.body("{\"gameName\":\"\"}");
            return serializer.toJson(createResponse);
        }
        catch (IllegalArgumentException exception) {
            response.status(400);
            return "{\"message\":\"Error: bad request\"}";
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
