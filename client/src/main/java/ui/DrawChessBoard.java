package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import models.GameData;

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
        boolean isWhite = false ;
        ChessBoard board = getBoard();
        drawChessBoard(out, isWhite, board);
    }

    static void drawChessBoard(PrintStream out, boolean isWhite, ChessBoard board) {
// set up the starter chess board will need to replace later with the actual game data and when switching from black to white
        out.print(ERASE_SCREEN);

//        ChessBoard board = getBoard();

        printHeading(out, isWhite);

        //Print Chess board
        if(isWhite){
            printWhiteChessBoard(out, board, isWhite);
        }
        else{
            printBlackBoard(out, board, isWhite);
        }
//        printChessBoard(out, board, isWhite);

        printHeading(out, isWhite);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void printBlackBoard(PrintStream out, ChessBoard board, boolean isWhite) {
        int number = 8;
        for (int row = 0; row < BOARD_SIZE; row++) {
            out.print(SET_BG_COLOR_WHITE);
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(" " + (row + 1) + " ");
            for (int col = BOARD_SIZE - 1; col >= 0; col--) {
                if((row + col) % 2 != 0) {
                    printPieces(out, board, row, col, SET_BG_COLOR_LIGHT_GREY);
                }
                else{
                    printPieces(out, board, row, col, SET_BG_COLOR_DARK_GREY);

                }
            }
            out.print(SET_BG_COLOR_WHITE);
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(" " + (row + 1) + " ");

            number = number - 1;

            out.print(RESET_BG_COLOR + "\n");
        }
    }

    private static ChessBoard getBoard() {
        ChessBoard chessBoard = new ChessBoard();
        chessBoard.resetBoard();
        return chessBoard;
    }


    private static void printWhiteChessBoard(PrintStream out, ChessBoard board, boolean isWhite) {
        int number = 8;

        for (int row = BOARD_SIZE - 1; row >= 0; row--) {
            out.print(SET_TEXT_COLOR_BLACK);
            printNumbers(out, number);

            for (int col = 0; col < BOARD_SIZE; col++) {
                if((row + col) % 2 != 0) {
                    printPieces(out, board, row , col , SET_BG_COLOR_LIGHT_GREY);
                }
                else{
                    printPieces(out, board, row , col , SET_BG_COLOR_DARK_GREY);

                }
            }
            out.print(SET_TEXT_COLOR_BLACK);
            printNumbers(out, number);
            number = number - 1;

            out.print(RESET_BG_COLOR + "\n");
        }
    }

    private static void printPieces(PrintStream out, ChessBoard board, int row, int col, String setBgColorLightGrey) {
        out.print(setBgColorLightGrey);
        ChessPosition chessPosition = new ChessPosition(row +1, col + 1);

        if(board.getPiece(chessPosition) == null) {
            out.print("   ");
        }
        else {
            ChessPiece chessPiece = board.getPiece(chessPosition);
            ChessGame.TeamColor teamColor = chessPiece.getTeamColor();
            if(teamColor == ChessGame.TeamColor.WHITE){
                out.print(SET_TEXT_COLOR_BLUE);

                out.print(" " + board.getPiece(chessPosition) + " ");
            }
            else {
                out.print(SET_TEXT_COLOR_RED);
                out.print(" " + board.getPiece(chessPosition) + " ");
            }
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
