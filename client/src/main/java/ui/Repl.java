package ui;

import java.util.Scanner;
import ui.EscapeSequences;

public class Repl {
    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to chess. Sign in to start.");

        Scanner scanner = new Scanner(System.in);
        var line = "";
        String prefix;
        //String username;
        while (!line.equals("quit")) {
            if (client.state == State.SIGNEDOUT) {
                prefix = EscapeSequences.SET_TEXT_COLOR_RED + "[LOGGED OUT]" +
                        EscapeSequences.RESET_TEXT_COLOR + " >>> ";
            }
            else {
                //username = "user"; //figure out how to set actual username
                prefix = EscapeSequences.SET_TEXT_COLOR_RED + "[CHESS]" +
                        EscapeSequences.RESET_TEXT_COLOR + " >>> ";//mb need color for user too
            }
            System.out.print(prefix);
            line = scanner.nextLine();

            try {
                String result = client.findCommand(line);
                System.out.println(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }
}
