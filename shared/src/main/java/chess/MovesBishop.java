package chess;

import java.util.Collection;
import java.util.ArrayList;

public class MovesBishop extends MoveCalculator {
    public static Collection<ChessMove> getMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(pos);
        addLinear(board, moves, pos, piece, 1, 1);
        addLinear(board, moves, pos, piece, 1, -1);
        addLinear(board, moves, pos, piece, -1, 1);
        addLinear(board, moves, pos, piece, -1, -1);
        return moves;
    }
}
