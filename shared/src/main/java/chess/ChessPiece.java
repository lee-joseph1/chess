package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor color;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.color = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        //throw new RuntimeException("Not implemented");
        //switch cases when each move calculator completed
        return switch (type){
            case KNIGHT, PAWN -> null;
            case BISHOP -> MovesBishop.getMoves(board, myPosition);
            case ROOK -> MovesRook.getMoves(board, myPosition);
            case QUEEN -> MovesQueen.getMoves(board, myPosition);
            case KING -> MovesKing.getMoves(board, myPosition);
        };
    }

    @Override
    public String toString() {
        return switch(type) {
            case KING -> color == ChessGame.TeamColor.WHITE ? "K" : "k";
            case QUEEN -> color == ChessGame.TeamColor.WHITE ? "Q" : "q";
            case BISHOP -> color == ChessGame.TeamColor.WHITE ? "B" : "b";
            case KNIGHT -> color == ChessGame.TeamColor.WHITE ? "N" : "n";
            case ROOK -> color == ChessGame.TeamColor.WHITE ? "R" : "r";
            case PAWN -> color == ChessGame.TeamColor.WHITE ? "P" : "p";
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return color == that.color && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }
}


