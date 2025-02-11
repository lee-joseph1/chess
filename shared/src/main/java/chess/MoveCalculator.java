package chess;

import java.util.Collection;

public class MoveCalculator {
    public static boolean isOnBoard(ChessPosition pos) {
        int row = pos.getRow();
        int col = pos.getColumn();
        return row > 0 && row <= 8 && col > 0 && col <= 8;
    }

    public static boolean ontoEnemyPiece(final ChessPosition pos, final ChessBoard board, final ChessPiece piece) {
        ChessPiece target = board.getPiece(pos);
        return target.getTeamColor() != piece.getTeamColor();
    }

    public static void addLinear(ChessBoard board, Collection<ChessMove> moves,
                                   ChessPosition pos, ChessPiece piece, int dr, int dc) {//delta row, delta col
        int attemptRow = pos.getRow() + dr;
        int attemptCol = pos.getColumn() + dc;
        while (isOnBoard(new ChessPosition(attemptRow, attemptCol))) {
            ChessPosition attemptPos = new ChessPosition(attemptRow, attemptCol);
            if (board.getPiece(attemptPos) == null) {
                moves.add(new ChessMove(pos, attemptPos, null));
            } //if empty, valid
            else if (ontoEnemyPiece(attemptPos, board, piece)) {
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

    public static ChessMove kingNight (ChessBoard board, ChessPosition pos, int targR, int targC){
        if (isOnBoard(new ChessPosition(targR, targC))) {
            ChessPosition targetPos = new ChessPosition(targR, targC);
            ChessPiece targetPiece = board.getPiece(targetPos);
            if (targetPiece == null || ontoEnemyPiece(targetPos, board, board.getPiece(pos))) {
                return new ChessMove(pos, targetPos, null);
            }
        }
        return null;
    }
}
