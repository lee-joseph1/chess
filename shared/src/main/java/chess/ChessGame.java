package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;
    private boolean whiteCanCastleLong;
    private boolean blackCanCastleLong;
    private boolean whiteCanCastleShort;
    private boolean blackCanCastleShort;
    private boolean enPassant;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        setTeamTurn(TeamColor.WHITE);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //throw new RuntimeException("Not implemented");
        //get list of valid moves from the move calculator and validity checkers
        // ^^^^more like see if i even care about those files still lelelel smh my head i think im overcomplicating for myself
        //pretend to remove the piece (as capturing would replace as a barrier) and see if in check
        //think about if this holds true for en passant as well since the replaced piece is not in same position !!!!!
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(startPosition);
        ChessGame.TeamColor teamColor = piece.getTeamColor();
        for (ChessMove move : piece.pieceMoves(board, startPosition)) {
//            ChessBoard saveBoard = board;
//            if (board.getPiece(move.getEndPosition()) != null) {
//                board.addPiece(move.getEndPosition(), null);
//            }
//            board.addPiece(move.getEndPosition(), piece);
//            board.addPiece(move.getStartPosition(), null);
            ChessPiece savePiece = board.getPiece(move.getEndPosition());
            board.addPiece(move.getEndPosition(), piece);
            board.addPiece(move.getStartPosition(), null);
            if (!isInCheck(teamColor)) {
                moves.add(move);
            }
            board.addPiece(move.getStartPosition(), piece);
            board.addPiece(move.getEndPosition(), savePiece);
        }
        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //throw new RuntimeException("Not implemented");
        //check all valid moves
            //if none, stalemate condition?
        //make sure its the right color for the turn, valid move
        //!!!!also check for promotion from the move
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece piece = board.getPiece(start);
        if (piece == null) {
            throw new InvalidMoveException();
        }
        if (validMoves(start).contains(move) && piece.getTeamColor() == teamTurn) {
            board.addPiece(start, null);
            if (move.getPromotionPiece() != null) {
                board.addPiece(end, new ChessPiece(teamTurn, move.getPromotionPiece()));
            }
            else {
                board.addPiece(end, piece);
            }
            if (teamTurn == TeamColor.BLACK) {
                teamTurn = TeamColor.WHITE;
            }
            else {
                teamTurn = TeamColor.BLACK;
            }
        }
        else {
            throw new InvalidMoveException();
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //throw new RuntimeException("Not implemented");
        ChessPosition kingPos = findKing(teamColor);
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition currentPosition = new ChessPosition(row, col);
                ChessPiece currentPiece = board.getPiece(currentPosition);
                if (currentPiece != null && currentPiece.getTeamColor() != teamColor) {
                    for (ChessMove threat : currentPiece.pieceMoves(board, currentPosition)) {
                        if (threat.getEndPosition().equals(kingPos)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private ChessPosition findKing(TeamColor teamColor) {
        ChessPosition kingPos = new ChessPosition(9, 9);
        for (int row = 1; row < 9 && kingPos.getRow() == 9; row++) {
            for (int col = 1; col < 9  && kingPos.getRow() == 9; col++) {
                ChessPiece currentPiece = board.getPiece(new ChessPosition(row, col));
                if (currentPiece != null && currentPiece.getPieceType() == ChessPiece.PieceType.KING
                        && currentPiece.getTeamColor() == teamColor) {
                    kingPos = new ChessPosition(row, col);
                }
            }
        }
        return kingPos;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        //if in check
        //try every possible move for your team (getValidMoves)
        //if no valid moves, set gameOver true, return true
        // umm these are literally just the check + stalemate conditions?? so yeah
        //return false;
        return stalemateCondition(teamColor) && isInCheck(teamColor);
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        //throw new RuntimeException("Not implemented");
        //if valid moves empty, inCHeck false
        //here its defined where in check doesn't matter
        //since valid moves is position dependent just iterate over all pieces for
        //that color & if we find one valid move then its a nah
        //haha nvm check is important so moved it to a new method
        return !isInCheck(teamColor) && stalemateCondition(teamColor);
    }

    public boolean stalemateCondition(TeamColor teamColor) {
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                if (board.getPiece(pos)  != null && board.getPiece(pos).getTeamColor() == teamColor) {
                    if (!validMoves(pos).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
        //throw new RuntimeException("Not implemented");
    }
}
