package ui;

import java.util.Scanner;
import ui.EscapeSequences;

public class Repl {
    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to the pet store. Sign in to start.");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        String prefix;
        String username;
        while (!result.equals("quit")) {
            if (client.state == State.SIGNEDOUT) {
                prefix = EscapeSequences.SET_TEXT_COLOR_RED + "[LOGGED OUT]" +
                        EscapeSequences.RESET_TEXT_COLOR + " >>> ";
            }
            else {
                username = result; //figure out how to set actual username
                prefix = EscapeSequences.SET_TEXT_COLOR_RED + "[" + username + "]" +
                        EscapeSequences.RESET_TEXT_COLOR + " >>> ";//mb need color for user too
            }
            System.out.print(prefix);
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }
}
