import chess.*;
import com.google.gson.Gson;
import spark.*;
import java.util.*;
//import chess.server.src.main.java.server.Server;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
    }
}