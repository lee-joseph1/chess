package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.UserData;
//import model.AuthData;
import service.UserService;
import service.request.RegRequest;
import service.response.RegResponse;
import spark.Route;
import spark.Request;
import spark.Response;
public class RegHandler implements Route{
    private UserService userService;
    private Gson gson = new Gson();

    public RegHandler(UserService userService) {
        this.userService = userService;
    }
    @Override
    public Object handle(Request request, Response response){
        try {
            String requestBody = request.body();
            RegRequest regRequest = gson.fromJson(requestBody, RegRequest.class);
            RegResponse result = userService.register(regRequest);
            response.status(200);
            return gson.toJson(result);
        }
        catch (IllegalArgumentException exception) {
            response.status(400);
            return "{\"mssage\":\"Error: bad request\"}";
        }
        catch (DataAccessException exception) {
            if (exception.getMessage().contains("User already exists")
                    || exception.getMessage().contains("already taken"))
            {
                response.status(403);
                return "{\"message\":\"Error: already taken\"}";
            }
            else {
                response.status(500);
                return "{\"message\":\"Error: " + exception.getMessage() + "\"}";
            }

        }
        catch (Exception exception) {
            response.status(500);
            return "{\"message\":\"Error: " + exception.getMessage() + "\"}";
        }
    }
}
