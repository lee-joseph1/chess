package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MovesQueen extends MoveCalculator{
    //get valid bishop moves
    //get valid rook moves
    //compile list
    //run all validity checks on each
    //compile final list
    public static Collection<ChessMove> getMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> moves = new ArrayList<>();
        moves.addAll(MovesRook.getMoves(board, pos));
        moves.addAll(MovesBishop.getMoves(board, pos));
        return moves;
    }
}