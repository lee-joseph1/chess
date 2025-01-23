package chess;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

public class MoveCalculator {
    public static boolean isOnBoard(ChessPosition pos) {
        int row = pos.getRow();
        int col = pos.getColumn();
        return row > 0 && row <= 8 && col > 0 && col <= 8;
    }

    public static boolean ontoFriendlyPiece(final ChessPosition pos, final ChessBoard board, final ChessPiece piece) {
        ChessPiece target = board.getPiece(pos);
        return piece != null && target.getTeamColor() == piece.getTeamColor();
    }

    public static Collection<ChessMove> getMoves(ChessBoard board, ChessPosition pos, ChessPiece.PieceType type) {
        Collection<ChessMove> moves = new ArrayList<>();
        moves.add(new ChessMove(pos, pos, type));
        return moves;
    };
}
