package ui.commands;

import java.util.Scanner;
import ui.EscapeSequences.*;

public class prelogin {
    //login, register, quit, (help)
    private static boolean loggedOut;
    private static String authToken = null;

    public prelogin() {
        boolean loggedOut = true; //ensure always starts logged out
        String authToken = null; //should this be only when logout to make sure table stays in sync?

    }
    public void start() {
        Scanner scanner = new Scanner(System.in);
        String command;
        String prefix = ""
    }

}
