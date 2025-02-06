package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;
    public static boolean whiteCanCastleLong;
    public static boolean blackCanCastleLong;
    public static boolean whiteCanCastleShort;
    public static boolean blackCanCastleShort;
    public static boolean enPassant;
    public static ChessPosition enPassantSquare;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        setTeamTurn(TeamColor.WHITE);
        whiteCanCastleLong = true;
        whiteCanCastleShort = true;
        blackCanCastleLong = true;
        blackCanCastleShort = true;
        enPassant = false;
        checkCastlePresetBoard(board);
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
        // ^^^^more like see if I even care about those files still lelelel smh my head I think im overcomplicating for myself
        //pretend to remove the piece (as capturing would replace as a barrier) and see if in check
        //think about if this holds true for en passant as well since the replaced piece is not in same position !!!!!
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return moves;
        }
        ChessGame.TeamColor teamColor = piece.getTeamColor();
        for (ChessMove move : piece.pieceMoves(board, startPosition)) {
            ChessPosition start = move.getStartPosition();
            ChessPosition end = move.getEndPosition();
            if (tryEnPassant(move)) {
                moves.add(move);
            }
            else if (tryCastle(move)) {
                moves.add(move);
            }
            else {
                ChessPiece savePiece = board.getPiece(move.getEndPosition());
                board.addPiece(end, piece);
                board.addPiece(start, null);
                if (!isInCheck(teamColor)) {
                    moves.add(move);
                }
                board.addPiece(move.getStartPosition(), piece);
                board.addPiece(move.getEndPosition(), savePiece);
            }
        }
        System.out.print(moves);
        return moves;
    }

    public boolean tryEnPassant(ChessMove move) {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece piece = board.getPiece(start);
        ChessGame.TeamColor teamColor = piece.getTeamColor();
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN && enPassant && end.equals(enPassantSquare)
                && board.getPiece(end) == null) {
            board.addPiece(end, piece);
            ChessPosition capture = new ChessPosition(start.getRow(), end.getColumn());
            ChessPiece savePiece = board.getPiece(capture);
            board.addPiece(capture, null);
            board.addPiece(start, null);
            if (!isInCheck(teamColor)) {
                return true;
            }
            board.addPiece(move.getStartPosition(), piece);
            board.addPiece(capture, savePiece);
            board.addPiece(move.getEndPosition(), null);
        }
        return false;
    }

    public boolean tryCastle(ChessMove move) {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece king = board.getPiece(start);
        ChessGame.TeamColor teamColor = king.getTeamColor();
        if (king.getPieceType() == ChessPiece.PieceType.KING && Math.abs(end.getColumn() - start.getColumn()) > 1) {
            if (isInCheck(teamColor)) {
                return false;
            }
            board.addPiece(start, null);//get rid of king
            if (start.getRow() > end.getRow()) {//short
                ChessPosition mid = new ChessPosition(start.getRow(), 6);
                if (board.getPiece(mid) != null || board.getPiece(end) != null) {
                    return false; //middle 2 spaces must be empty
                }
                board.addPiece(mid, king); //check moving through check
                if (isInCheck(teamColor)) {
                    return false;
                }
                board.addPiece(mid, null); //keep moving
                board.addPiece(new ChessPosition(start.getRow(), 6), new ChessPiece(teamColor, ChessPiece.PieceType.ROOK));
                board.addPiece(new ChessPosition(start.getRow(), 8), null);
                board.addPiece(end, king);
                if (!isInCheck(teamColor)) {
                    return true;
                }
                board.addPiece(new ChessPosition(start.getRow(), 6), null);
                board.addPiece(new ChessPosition(start.getRow(), 8), new ChessPiece(teamColor, ChessPiece.PieceType.ROOK));
                board.addPiece(end, null);
                board.addPiece(start, king);
            }
            else {//long
                ChessPosition mid = new ChessPosition(start.getRow(), 4);
                if (!(board.getPiece(mid) == null && board.getPiece(end) == null)) {
                    return false;
                }
                board.addPiece(mid, king);
                if (isInCheck(teamColor)) {
                    return false;
                }
                board.addPiece(mid, null);
                board.addPiece(new ChessPosition(start.getRow(), 4), new ChessPiece(teamColor, ChessPiece.PieceType.ROOK));
                board.addPiece(new ChessPosition(start.getRow(), 1), null);
                board.addPiece(end, king);
                if (!isInCheck(teamColor)) {
                    return true;
                }
                board.addPiece(new ChessPosition(start.getRow(), 4), null);
                board.addPiece(new ChessPosition(start.getRow(), 1), new ChessPiece(teamColor, ChessPiece.PieceType.ROOK));
                board.addPiece(end, null);
                board.addPiece(start, king);
            }
        }
        return false;
    }

    public void checkCastlePresetBoard(ChessBoard board) {
        ChessPosition whiteKing = new ChessPosition(1, 5);
        ChessPosition blackKing = new ChessPosition(8, 5);
        ChessPosition whiteRook1 = new ChessPosition(1, 1);
        ChessPosition blackRook1 = new ChessPosition(8, 1);
        ChessPosition whiteRook8 = new ChessPosition(1, 8);
        ChessPosition blackRook8 = new ChessPosition(8, 8);
        if (!Objects.equals(board.getPiece(whiteKing), new ChessPiece(TeamColor.WHITE, ChessPiece.PieceType.KING))) {
            whiteCanCastleShort = false;
            whiteCanCastleLong = false;
        }
        if (!Objects.equals(board.getPiece(blackKing), new ChessPiece(TeamColor.BLACK, ChessPiece.PieceType.KING))) {
            blackCanCastleShort = false;
            blackCanCastleLong = false;
        }
        if (!Objects.equals(board.getPiece(whiteRook1), new ChessPiece(TeamColor.WHITE, ChessPiece.PieceType.ROOK))) {
            whiteCanCastleLong = false;
        }
        if (!Objects.equals(board.getPiece(whiteRook8), new ChessPiece(TeamColor.WHITE, ChessPiece.PieceType.ROOK))) {
            whiteCanCastleShort = false;
        }
        if (!Objects.equals(board.getPiece(blackRook1), new ChessPiece(TeamColor.BLACK, ChessPiece.PieceType.ROOK))) {
            blackCanCastleLong = false;
        }
        if (!Objects.equals(board.getPiece(blackRook8), new ChessPiece(TeamColor.BLACK, ChessPiece.PieceType.ROOK))) {
            blackCanCastleShort = false;
        }
    }

    //simplify method to trymove, trypassant, trycastle
    //consider using a copy constructor instead of savePiece

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
        //make sure it's the right color for the turn, valid move
        //!!!!also check for promotion from the move
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece piece = board.getPiece(start);
        if (piece == null) {
            throw new InvalidMoveException();
        }
        if (validMoves(start).contains(move) && piece.getTeamColor() == teamTurn) {
            board.addPiece(start, null);
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN && enPassant
                    && board.getPiece(end) == null) {
                board.addPiece(end, piece);
                ChessPosition capture = new ChessPosition(start.getRow(), end.getColumn());
                board.addPiece(capture, null);
                board.addPiece(start, null);
            }
//            else if (piece.getPieceType() == ChessPiece.PieceType.KING &&
//                    Math.abs(end.getColumn() - start.getColumn()) > 1) {
//                ChessGame.TeamColor teamColor = piece.getTeamColor();
//                board.addPiece(start, null);
//                if (start.getRow() > end.getRow()) {//short
//                    board.addPiece(new ChessPosition(start.getRow(), 6), new ChessPiece(teamColor, ChessPiece.PieceType.ROOK));
//                    board.addPiece(new ChessPosition(start.getRow(), 8), null);
//                    board.addPiece(end, piece);
//                }
//                else {//long
//                    board.addPiece(new ChessPosition(start.getRow(), 4), new ChessPiece(teamColor, ChessPiece.PieceType.ROOK));
//                    board.addPiece(new ChessPosition(start.getRow(), 1), null);
//                    board.addPiece(end, piece);
//                }
//            }
            else if (move.getPromotionPiece() != null) {
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
            updateEnPassant(move, piece);
            updateCastling(move, piece);
        }
        else {
            throw new InvalidMoveException();
        }
    }

    //similar simplification

    public void updateEnPassant(ChessMove move, ChessPiece piece) {
        //if just moved two squares set to true
        //else set to false
        //ChessPiece piece = board.getPiece(move.getStartPosition());
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            if (Math.abs(start.getRow() - end.getRow()) == 2) {
                enPassant = true;
                enPassantSquare = end;
            }
            else {
                enPassant = false;
                enPassantSquare = null;
            }
        }
        else {
            enPassant = false;
            enPassantSquare = null;
        }
    }
    public void updateCastling(ChessMove move, ChessPiece piece) {
        //if moving rook set false for that side
        //if moving king set false (will move king for initial castle move)
        //will need serious refactoring of things I think
        ChessPosition start = move.getStartPosition();
        ChessGame.TeamColor color = piece.getTeamColor();
        if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            if (start.getColumn() == 8) {
                if (color == TeamColor.WHITE) {
                    whiteCanCastleShort = false;
                }
                else {
                    blackCanCastleShort = false;
                }
            }
            if (start.getColumn() == 1) {
                if (color == TeamColor.WHITE) {
                    whiteCanCastleLong = false;
                }
                else {
                    blackCanCastleLong = false;
                }
            }
        }
        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            if (color == TeamColor.WHITE) {
                whiteCanCastleLong = false;
                whiteCanCastleShort = false;
            }
            else {
                blackCanCastleLong = false;
                blackCanCastleShort = false;
            }
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
        //that color & if we find one valid move then it's a nah
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
