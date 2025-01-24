package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Arrays;

public class MovesNight extends MoveCalculator{
    //allow rank+-2 file +-1 OR file +-2 rank +-1
    //run validity checks invalid target, into check
    //does not need blocked path check
    static List<Integer> longSide = Arrays.asList(-2, 2);
    static List<Integer> shortSide = Arrays.asList(-1, 1);
    public static Collection<ChessMove> getMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> moves = new ArrayList<>();
        for (int dr : longSide) {//assuming long move on row
            int targR = pos.getRow() + dr;
            for (int dc : shortSide) {
                int targC = pos.getColumn() + dc;
                ChessMove ret = kingNight(board, pos, targR, targC);
                if (ret != null) moves.add(ret);
            }
        }
        for (int dr : shortSide) {//assuming long move on col
            int targR = pos.getRow() + dr;
            for (int dc : longSide) {
                int targC = pos.getColumn() + dc;
                ChessMove ret = kingNight(board, pos, targR, targC);
                if (ret != null) moves.add(ret);
            }
        }
        return moves;
    }
}
