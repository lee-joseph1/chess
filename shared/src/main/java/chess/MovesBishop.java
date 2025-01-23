package chess;

import java.util.Collection;
import java.util.ArrayList;

public class MovesBishop extends MoveCalculator {
    //allow diagonals so
    //FROM starting space
    //EITHER:
        //r+1 f+1 repeat (until rank or file >=8
        //r+1 f-1 repeat (until rank >=8 file <=0)
        // r-1 f+1 repeat (r<=0f>=8)
        //r-1 f-1 repeat (rf<=0)
    //run validity checks (all)
    //compile final list of valid moves
    public static Collection<ChessMove> getMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(pos);
        addDiag(board, moves, pos, piece, 1, 1);
        addDiag(board, moves, pos, piece, 1, -1);
        addDiag(board, moves, pos, piece, -1, 1);
        addDiag(board, moves, pos, piece, -1, -1);
        return moves;
    }
    private static void addDiag(ChessBoard board, Collection<ChessMove> moves,
                         ChessPosition pos, ChessPiece piece, int dr, int dc) {//delta row, delta col
        int attemptRow = pos.getRow() + dr;
        int attemptCol = pos.getColumn() + dc;
        while (isOnBoard(new ChessPosition(attemptRow, attemptCol))) {
            ChessPosition attemptPos = new ChessPosition(attemptRow, attemptCol);
            if (board.getPiece(attemptPos) == null) {
                moves.add(new ChessMove(pos, attemptPos, null));
            } //if empty, valid
            else if (!ontoFriendlyPiece(attemptPos, board, piece)) {
                moves.add(new ChessMove(pos, attemptPos, null));
                break;
            } //if enemy piece, can take, but can't move through
            else {
                break;
            } //cannot move through friendly pieces
            attemptRow += dr;
            attemptCol += dc;
        }
    }
}
