package ui;

import chess.ChessGame;

import static ui.EscapeSequences.*;

public class Draw {
    public static String whiteSquare = EscapeSequences.SET_BG_COLOR_LIGHT_GREEN;
    public static String blackSquare = EscapeSequences.SET_BG_COLOR_DARK_GREEN;
    public static String borderSquare = EscapeSequences.SET_BG_COLOR_CREAM;
    public static String label = EscapeSequences.SET_TEXT_COLOR_LABEL;
    public static String resetBg = EscapeSequences.RESET_BG_COLOR;
    public ChessGame game;
    //public ChessBoard board = game.getBoard();
    public static boolean isWhiteSpace;
    static String[] fileLabels = new String[]{EMPTY, label + " a ", label + " b ", label + "  c ", label + " d ",
            label + "  e ", label +  "  f ", label + " g ", label + "  h ", EMPTY};
    static String[][] board = {
            fileLabels,
            {label + " 8 ", BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_QUEEN, BLACK_KING, BLACK_BISHOP,
                    BLACK_KNIGHT, BLACK_ROOK, label + " 8 "},
            {label + " 7 ", BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN,
                    BLACK_PAWN, BLACK_PAWN, label + " 7 "},
            {label + " 6 ", EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, label + " 6 "},
            {label + " 5 ", EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, label + " 5 "},
            {label + " 4 ", EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, label + " 4 "},
            {label + " 3 ", EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, label + " 3 "},
            {label + " 2 ", WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN,
                    WHITE_PAWN, WHITE_PAWN, label + " 2 "},
            {label + " 1 ", WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP, WHITE_QUEEN, WHITE_KING, WHITE_BISHOP,
                    WHITE_KNIGHT, WHITE_ROOK, label + " 1 "},
            fileLabels};
    //make this read in from the actual chess board with a border inside a loop
    //make a switch case for reading from the chessbaord to the actual chess characters

    public static void drawBoardWhite() {
        for (int i = 0; i <= 9; i++) {
            for (int j = 0; j <= 9; j++) {
                checkBounds(i, j);
            }
            System.out.println(resetBg);
        }
    }

    public static void drawBoardBlack() {
        for (int i = 9; i >= 0; i--) {
            for (int j = 9; j >= 0; j--) {
                checkBounds(i, j);
            }
            System.out.println(resetBg);
        }
    }

    private static void checkBounds(int i, int j) {
        if (i == 0 || i == 9 || j == 0 || j == 9) {
            System.out.print(borderSquare + board[i][j]);
        }
        else {
            isWhiteSpace = (i + j) % 2 == 0;
            System.out.print((isWhiteSpace ? whiteSquare : blackSquare) + board[i][j] + resetBg);
        }
    }
}