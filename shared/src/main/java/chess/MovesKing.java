package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MovesKing extends MoveCalculator{
    //allow any two r+-0,1, f+-0,1
    //run validity checks (all)
    //remember that moving into check is handled by CheckIntoCheck class
    //compile final list
    public static Collection<ChessMove> getMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        for (int dr = -1; dr <=1; dr++) {
            int targR = pos.getRow() + dr;
            for (int dc = -1; dc <=1; dc++) {
                if (dr == 0 && dc == 0) continue;
                int targC = pos.getColumn() + dc;
//                if (isOnBoard(new ChessPosition(targR, targC))) {
//                    ChessPosition targPos = new ChessPosition(targR, targC);
//                    ChessPiece targPiece = board.getPiece(targPos);
//                    if (targPiece == null || !ontoFriendlyPiece(targPos, board, board.getPiece(pos))) {
//                        moves.add(new ChessMove(pos, targPos, null));
//                    }
//                }
                ChessMove ret = kingNight(board, pos, targR, targC);
                if (ret != null) moves.add(ret);
            }
        }
        return moves;
    }
}
