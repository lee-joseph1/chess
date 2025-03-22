package ui.commands;

import java.util.Scanner;

public class prelogin {
    //login, register, quit, (help)
    private static boolean loggedOut;
    private static String authToken = null;

    public prelogin() {
        loggedOut = true; //ensure always starts logged out
        authToken = null; //should this be only when logout to make sure table stays in sync?
    }
    public void start() {
        Scanner scanner = new Scanner(System.in);
        String command;
        String prefix = "\u001B[20m"
    }

}
