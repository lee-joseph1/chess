package handler;

import service.AuthService;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler implements Route {

    public LoginHandler(AuthService authService, UserService userService) {}

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return null;
    }
}
