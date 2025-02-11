package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.whiteCanCastleLong;
import static chess.ChessGame.whiteCanCastleShort;
import static chess.ChessGame.blackCanCastleLong;
import static chess.ChessGame.blackCanCastleShort;

public class MovesKing extends MoveCalculator{
    //allow any two r+-0,1, f+-0,1
    //run validity checks (all)
    //remember that moving into check is handled by CheckIntoCheck class
    //compile final list
    public static Collection<ChessMove> getMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> moves = new ArrayList<>();
        for (int dr = -1; dr <= 1; dr++) {
            int targetR = pos.getRow() + dr;
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                int targetC = pos.getColumn() + dc;
                ChessMove ret = kingNight(board, pos, targetR, targetC);
                if (ret != null) moves.add(ret);
            }
        }
        return moves;
    }
}
