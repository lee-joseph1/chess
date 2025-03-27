package ui;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import server.ServerFacade;
import server.ListResponse2;

import java.util.HashMap;
import java.util.Objects;

public class ChessClient {
    private final ServerFacade facade;
    public State state = State.SIGNEDOUT;
    private AuthData auth;
    private HashMap<Integer, GameData> chessGames = new HashMap<>();

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
        String console = EscapeSequences.SET_TEXT_COLOR_CONSOLE;
        String highlight = EscapeSequences.SET_TEXT_COLOR_HIGHLIGHT;
        String base = EscapeSequences.SET_TEXT_COLOR_BASE;
        if (state == State.SIGNEDOUT) {
            //help, register, login
            //prelogin(cmd, args, console, highlight, base);
            String x = preLogin(cmd, args, console, highlight, base);
            if (x != null) {
                return x;
            }
        }
        else {
            String x = postLogin(cmd, args, console, base, highlight);
            if (x != null) {
                return x;
            }
        }
        return "";
    }

    private String postLogin(String cmd, String[] args, String console, String base, String highlight) throws Exception {
        switch (cmd) {
            case "help" -> {
                System.out.println(console + "logout " + base + " - log out");
                System.out.println(console + "create " + highlight + "<GAME_NAME> " + base + " - create a new chess game");
                System.out.println(console + "list" + base + " - list current games");
                System.out.println(console + "join" + highlight + "<GAME_NUMBER> [WHITE|BLACK]" + base + " - join game as selected color");
                System.out.println(console + "observe" + highlight + "<GAME_NUMBER> " + base + " - watch a game");
                System.out.println(console + "quit" + base + " - quit");
                System.out.println(console + "help" + base + " - list available commands");
                return "";
            }
            case "logout" -> {
                facade.logout(auth);
                state = State.SIGNEDOUT;
                return "user " + auth.username() + " logged out";
            }
            case "create" -> {
                if (args.length != 1) {
                    System.out.println(console + "please choose a game name");
                    //return "error creating game - bad input";
                    throw new Exception("Error: bad input for login");
                }
                GameData game = facade.create(auth, args[0]);
                return "game created";
            }
            case "list" -> {
                ListResponse2 gameList = facade.list(auth);
                for (int i = 0; i < gameList.games().size(); i++) {
                    chessGames.put(i+1, gameList.games().get(i));
                }
                for (HashMap.Entry<Integer, GameData> item : chessGames.entrySet()){
                    GameData game = item.getValue();
                    System.out.println(item.getKey() + ": " + game.gameName() + " WHITE: " +
                            game.whiteUsername() + ", BLACK: " + game.blackUsername());
                }
                return "";
            }
            case "join" -> {
                if (args.length != 2) {
                    System.out.println(console + "please choose a game and color");
                    //return "error creating game - bad input";
                    throw new Exception("Error: bad input for join");
                }
                ListResponse2 gameList = facade.list(auth);
                for (int i = 1; i < gameList.games().size(); i++) {
                    chessGames.put(i, gameList.games().get(i-1));
                } //need to fetch all games again in most recent state
                try {
                    if (Integer.parseInt(args[0]) < 1 || Integer.parseInt(args[0]) > chessGames.size()) {
                        throw new Exception("Error: invalid game number");
                    }
                } catch (Exception ex) {
                    throw new Exception("Error: bad input for join");
                }
                if ((!Objects.equals(args[1], "WHITE")) && (!Objects.equals(args[1], "BLACK"))) {
                    throw new Exception("Error: please choose a valid color, in caps");
                }
                facade.join(auth, args, chessGames);
                GameData game = chessGames.get(Integer.parseInt(args[0]));
                if (args[1].equals("WHITE")) {
                    //System.out.println("print board from white here");
                    Draw.drawBoardWhite();
                    return "";
                } else if (args[1].equals("BLACK")) {
                    //System.out.println("print board from black here");
                    Draw.drawBoardBlack();
                    return "";
                }
            }
            case "observe" -> {
                if (args.length != 1) {
                    System.out.println(console + "please choose a game");
                    //return "error creating game - bad input";
                    throw new Exception("Error: bad input for observe");
                }
                ListResponse2 gameList = facade.list(auth);
                for (int i = 1; i < gameList.games().size(); i++) {
                    chessGames.put(i, gameList.games().get(i-1));
                } //need to fetch all games again in most recent state
                try {
                    if (Integer.parseInt(args[0]) < 1 || Integer.parseInt(args[0]) > chessGames.size()) {
                        throw new Exception("Error: invalid game number");
                    }
                } catch (Exception ex) {
                    throw new Exception("Error: bad input for observe");
                }
                for (int i = 1; i < gameList.games().size(); i++) {
                    chessGames.put(i, gameList.games().get(i-1));
                }
                GameData game = chessGames.get(Integer.parseInt(args[0]));
                Draw.drawBoardWhite();
                //return "< imagine fetching selected game & printing from white here >";
            }
        }
        return null;
    }

    private String preLogin(String cmd, String[] args, String console, String highlight, String base) throws Exception {
        switch (cmd) {
            case "help" -> {
                System.out.println(console + "register " + highlight + "<USERNAME> <PASSWORD> <EMAIL>" + base + " - create an account");
                System.out.println(console + "login " + highlight + "<USERNAME> <PASSWORD>" + base + " - login to existing account");
                System.out.println(console + "quit" + base + " - quit");
                System.out.println(console + "help" + base + " - list available commands");
                return "helped.";
            }
            case "register" -> {
                System.out.println("registering new user");
                if (args.length != 3) {
                    System.out.println(console + "please input a username, pasword, and email");
                    //return "error registering - bad input";
                    throw new Exception("Error: bad input for register");
                } else {
                    auth = facade.register(args);
                    state = State.SIGNEDIN;
                    return "user " + auth.username() + " registered and logged in";
                }
            }
            case "login" -> {
                if (args.length != 2) {
                    System.out.println(console + "please input a username and password");
                    //return "error logging in - bad input";
                    throw new Exception("Error: bad input for login");
                } else {
                    auth = facade.login(args);
                    state = State.SIGNEDIN;
                    return "user " + auth.username() + " logged in";
                }
            }
        }
        return null;
    }
}
