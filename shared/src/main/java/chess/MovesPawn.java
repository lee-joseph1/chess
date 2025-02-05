package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.enPassant;
import static chess.ChessGame.enPassantSquare;

public class MovesPawn extends MoveCalculator{
    public static Collection<ChessMove> getMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(pos);
        ChessGame.TeamColor color = piece.getTeamColor();
        int dir = 1;
        if (piece.getTeamColor().equals(ChessGame.TeamColor.BLACK)){
            dir = -1;
        }
        ChessPosition target = new ChessPosition(pos.getRow() + dir, pos.getColumn());
        if (isOnBoard(target) && board.getPiece(target) == null) {
            addMove(moves, pos, target);
            if ((color.equals(ChessGame.TeamColor.BLACK) && pos.getRow() == 7) ||
                    (color.equals(ChessGame.TeamColor.WHITE) && pos.getRow() == 2)) {
                ChessPosition target2 = new ChessPosition(pos.getRow() + 2 * dir, pos.getColumn());
                if (board.getPiece(target2) == null) {
                    addMove(moves, pos, target2);
                }
            }
        }
        addCapture(moves, board, pos, new ChessPosition(pos.getRow() + dir, pos.getColumn() + 1));
        addCapture(moves, board, pos, new ChessPosition(pos.getRow() + dir, pos.getColumn() - 1));
        if (enPassant) {
            ChessPosition target3 = new ChessPosition(enPassantSquare.getRow() + dir, enPassantSquare.getColumn());
            if (board.getPiece(target3) == null) {
                moves.add(new ChessMove(pos, target3, null));
            }
        }
        return moves;
    }

    public static void addMove(Collection<ChessMove> moves, ChessPosition pos,
                               ChessPosition target) {
        if (target.getRow() == 1 || target.getRow() == 8) {
            moves.add(new ChessMove(pos, target, ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(pos, target, ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(pos, target, ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(pos, target, ChessPiece.PieceType.KNIGHT));
        } else {
            moves.add(new ChessMove(pos, target, null));
        }
    }

    public static void addCapture(Collection<ChessMove> moves, ChessBoard board, ChessPosition pos,
                                  ChessPosition target) {
        if (isOnBoard(target) && board.getPiece(target) != null && ontoEnemyPiece(target, board, board.getPiece(pos))) {
                addMove(moves, pos, target);
        }
    }
}