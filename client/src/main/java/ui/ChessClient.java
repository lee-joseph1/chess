package ui;

import model.AuthData;

import java.io.IOException;

public class ChessClient {
    private final ServerFacade facade;
    public State state = State.SIGNEDOUT;
    private AuthData auth;

    public ChessClient(String serverURL) {
        facade = new ServerFacade(serverURL);
    }

    public void eval(String cmd) throws IOException {
        String RED = EscapeSequences.SET_TEXT_COLOR_RED;
        String YELLOW = EscapeSequences.SET_TEXT_COLOR_YELLOW;
        String RESET = EscapeSequences.RESET_TEXT_COLOR;
        String[] params = cmd.split("\\s+", 2);
        String command = params[0];
        String args = (params.length > 1) ? params[1]:"";
        if (state == State.SIGNEDOUT) {
            //help, register, login
            switch (cmd) {
                case "help" -> {
                    System.out.println(RED + "register " + YELLOW + "<USERNAME> <PASSWORD> <EMAIL>" + RESET + " - create an account");
                    System.out.println(RED + "login " + YELLOW + "<USERNAME> <PASSWORD>" + RESET + " - login to existing account");
                    System.out.println(RED + "quit" + RESET + " - quit");
                    System.out.println(RED + "help" + RESET + " - list available commands");
                }
                case "register" -> {
                    String[] registerArgs = args.split("\\s+");
                    if (registerArgs.length != 3) {
                        System.out.println(RED + "please input a username, pasword, and email");
                    } else {
                        auth = facade.register(registerArgs[0], registerArgs[1], registerArgs[2]);
                        if (auth.authToken() != null) {
                            state = State.SIGNEDIN;
                        }
                    }
                }
                case "login" -> {
                    String[] loginArgs = args.split("\\s+");
                    if (loginArgs.length != 2) {
                        System.out.println(RED + "please input a username and password");
                    } else {
                        auth = facade.Login(loginArgs[0], loginArgs[1]);
                        if (auth.authToken() != null) {
                            state = State.SIGNEDIN;
                        }
                    }
                }
            }
        }
        else {
            //help, create, join, list, logout
        }
    }
}
