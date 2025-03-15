package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class DrawChessBoard {
    private static final int BOARD_SIZE = 8;
    private static final int SQUARE_SIZE = 3;
    private static final int LINE_WIDTH = 1;

    private static final String EMPTY = "   ";
    private static final String LIGHT_SQUARE = SET_BG_COLOR_LIGHT_GREY;
    private static final String DARK_SQUARE = SET_BG_COLOR_DARK_GREY;

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        boolean isWhite = true;
        drawChessBoard(out, isWhite);
    }

    private static void drawChessBoard(PrintStream out, boolean isWhite) {
// set up the starter chess board will need to replace later with the actual game data and when switching from black to white
        String[][] board = getBoard(isWhite);

        printHeading(out, isWhite);

        //Print Chess board
        printChessBoard(out, board, isWhite);

        printHeading(out, isWhite);
    }

    private static String[][] getBoard(boolean isWhite) {
        if(isWhite){
            String[][] board = {
                    {BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_QUEEN, BLACK_KING, BLACK_BISHOP, BLACK_KNIGHT, BLACK_ROOK},
                    {BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN},
                    {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                    {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                    {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                    {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                    {WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN},
                    {WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP, WHITE_QUEEN, WHITE_KING, WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK}
            };
            return board;
        }
        else {
            String[][] board = {
                    {WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP, WHITE_QUEEN, WHITE_KING, WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK},
                    {WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN},
                    {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                    {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                    {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                    {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                    {BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN},
                    {BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_QUEEN, BLACK_KING, BLACK_BISHOP, BLACK_KNIGHT, BLACK_ROOK}
            };
            return board;
        }
    }

    private static void printChessBoard(PrintStream out, String[][] board, boolean isWhite) {
        int number = 8;
        for (int row = 0; row < BOARD_SIZE; row++) {
            if (isWhite) {
                printNumbers(out, number);
            }
            else{
                out.print(SET_BG_COLOR_WHITE);
                out.print(" " + (row+1) + " ");
            }
            for (int col = 0; col < BOARD_SIZE; col++) {
                if((row + col) % 2 != 0) {
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                    out.print(board[row][col]);
                }
                else{
                    out.print(SET_BG_COLOR_DARK_GREY);
                    out.print(board[row][col]);
                }
            }
            if (isWhite) {
                printNumbers(out, number);
            }
            else{
                out.print(SET_BG_COLOR_WHITE);
                out.print(" " + (row+1) + " ");
            }            number = number - 1;

            out.print(RESET_BG_COLOR + "\n");
        }
    }

    private static void printNumbers(PrintStream out, int number) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(" " + (number) + " ");
        number = number - 1;
    }

    private static void printHeading(PrintStream out, boolean isWhite) {
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(SET_BG_COLOR_WHITE);
        out.print("   ");
        if (isWhite){
            String heading = "a, b, c, d, e, f, g, h";
            for(char head = 'a'; head <= 'h'; head++){
                out.print(" " + head + " ");
            }
        }
        else{
            String heading = "h, g, f, e, d, c, b, a";
            for(char head = 'h'; head >= 'a'; head--){
                out.print(" " + head + " ");
            }
        }

        out.print("   ");
        out.print(RESET_BG_COLOR + "\n");
    }

}
