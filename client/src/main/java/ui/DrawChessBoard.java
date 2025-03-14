package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

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
        drawChessBoard(out);
    }

    private static void drawChessBoard(PrintStream out) {
// set up the starter chess board will need to replace later with the actual game data and when switching from black to white
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

        printHeading(out);

        //Print Chess board
        for (int row = 0; row < BOARD_SIZE; row++) {
            out.print(SET_BG_COLOR_WHITE);
            out.print(" " + (row+1) + " ");
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
            out.print(SET_BG_COLOR_WHITE);
            out.print(" " + (row+1) + " ");
            out.print(RESET_BG_COLOR + "\n");
        }
        printHeading(out);
    }

    private static void printHeading(PrintStream out) {
        String heading = "a, b, c, d, e, f, g, h";
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(SET_BG_COLOR_WHITE);

        out.print("   ");
        for(char head = 'a'; head <= 'h'; head++){
            out.print(" " + head + " ");
        }
        out.print("   ");
        out.print(RESET_BG_COLOR + "\n");
    }

}
