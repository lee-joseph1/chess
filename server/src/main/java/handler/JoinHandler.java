package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.GameService;
import service.requests.JoinRequest;
import service.responses.JoinResponse;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinHandler implements Route {
    private final GameService gameService;
    private final Gson serializer = new Gson();

    public JoinHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        try {
            JoinRequest joinRequest = serializer.fromJson(request.body(), JoinRequest.class);
            JoinResponse joinResponse = gameService.join(joinRequest, request.headers("authorization"));
            response.status(200);
            response.body("{ \"playerColor\":\"WHITE/BLACK\", \"gameID\": 1234 }");
            return serializer.toJson(joinResponse);
        }
        catch (IllegalArgumentException exception) {
            if (exception.getMessage().equals("bad request")) {
                response.status(400);
                return "{ \"message\": \"Error: bad request\" }";
            }
            else {
                response.status(403);
                return "{ \"message\": \"Error: already taken\" }";
            }
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
