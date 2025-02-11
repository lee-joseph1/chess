package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MovesKing extends MoveCalculator{
    public static Collection<ChessMove> getMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> moves = new ArrayList<>();
        for (int dr = -1; dr <= 1; dr++) {
            int targetR = pos.getRow() + dr;
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) {
                    continue;
                }
                int targetC = pos.getColumn() + dc;
                ChessMove ret = kingNight(board, pos, targetR, targetC);
                if (ret != null) {
                    moves.add(ret);
                }
            }
        }
        return moves;
    }
}
