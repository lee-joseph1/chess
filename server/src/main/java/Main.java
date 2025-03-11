import chess.*;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import spark.*;
import java.util.*;
//import chess.server.src.main.java.server.Server;
import server.Server;

public class Main {
    public static void main(String[] args) {
        try {
            var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            System.out.println("♕ 240 Chess Server: " + piece);
            Server server = new Server();
            server.run(8080);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}