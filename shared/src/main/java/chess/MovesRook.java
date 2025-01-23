package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MovesRook extends MoveCalculator {
    //allow laterals so
    //FROM starting space
    //EITHER:
    //r+1 repeat (until rank >=8
    //f+1 repeat (until file >=8)
    // r-1 repeat (r<=0)
    //f-1 repeat (f<=0)
    //run validity checks (all)
    //compile final list of valid moves
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
