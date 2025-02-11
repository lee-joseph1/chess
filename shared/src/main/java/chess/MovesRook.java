package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MovesRook extends MoveCalculator {
    public static Collection<ChessMove> getMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(pos);
        addLinear(board, moves, pos, piece, 1, 0);
        addLinear(board, moves, pos, piece, 0, 1);
        addLinear(board, moves, pos, piece, -1, 0);
        addLinear(board, moves, pos, piece, 0, -1);
        return moves;
    }
}
