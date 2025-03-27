package ui;

import model.AuthData;
import server.ServerFacade;

public class ChessClient {
    private final ServerFacade facade;
    public State state = State.SIGNEDOUT;
    private AuthData auth;

    public ChessClient(String serverURL) {
        facade = new ServerFacade(serverURL);
    }

    public String findCommand(String initCommand) throws Exception {
        String[] params = initCommand.split(" ");
        String[] args = new String[params.length - 1];
        System.arraycopy(params, 1, args, 0, params.length - 1);
        return eval(params[0], args);
    }

    public String eval(String cmd, String[] args) throws Exception {
        String RED = EscapeSequences.SET_TEXT_COLOR_RED;
        String YELLOW = EscapeSequences.SET_TEXT_COLOR_YELLOW;
        String RESET = EscapeSequences.RESET_TEXT_COLOR;
        if (state == State.SIGNEDOUT) {
            //help, register, login
            switch (cmd) {
                case "help" -> {
                    System.out.println(RED + "register " + YELLOW + "<USERNAME> <PASSWORD> <EMAIL>" + RESET + " - create an account");
                    System.out.println(RED + "login " + YELLOW + "<USERNAME> <PASSWORD>" + RESET + " - login to existing account");
                    System.out.println(RED + "quit" + RESET + " - quit");
                    System.out.println(RED + "help" + RESET + " - list available commands");
                    return "helped.";
                }
                case "register" -> {
                    System.out.println("registering new user");
                    if (args.length != 3) {
                        System.out.println(RED + "please input a username, pasword, and email");
                        return "error registering - bad input";
                        //throw new Exception("Error: bad input for register");
                    } else {
                        auth = facade.register(args);
                        state = State.SIGNEDIN;
                        return "user " + auth.username() + " registered and logged in";
                    }
                }
                case "login" -> {
                    if (args.length != 2) {
                        System.out.println(RED + "please input a username and password");
                        return "error logging in - bad input";
                        //throw new Exception("Error: bad input for login");
                    } else {
                        auth = facade.Login(args);
                        state = State.SIGNEDIN;
                        return "user " + auth.username() + " logged in";
                    }
                }
            }
        }
        else {
            //help, create, join, list, logout
        }
        return null;
    }
}
