package ui;

import chess.*;
import models.GameData;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import static ui.EscapeSequences.*;

public class DrawChessBoard {
    private static final int BOARD_SIZE = 8;
    private static final int SQUARE_SIZE = 3;
    private static final int LINE_WIDTH = 1;

    private static final String EMPTY = "   ";
    private static final String LIGHT_SQUARE = SET_BG_COLOR_LIGHT_GREY;
    private static final String DARK_SQUARE = SET_BG_COLOR_DARK_GREY;

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        boolean isWhite = false ;
        ChessBoard board = getBoard();
//        drawChessBoard(out, isWhite, board);
    }

    static void drawChessBoard(PrintStream out, boolean isWhite, ChessBoard board, boolean isHighlightMove) {
// set up the starter chess board will need to replace later with the actual game data and when switching from black to white
        out.print(ERASE_SCREEN);

//        ChessBoard board = getBoard();


        //Print Chess board
        if(isWhite){
            printWhiteChessBoard(out, board, isWhite, isHighlightMove);
        }
        else{
            printBlackBoard(out, board, isWhite, isHighlightMove);
        }
//        printChessBoard(out, board, isWhite);

        printHeading(out, isWhite);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void printBlackBoard(PrintStream out, ChessBoard board, boolean isWhite, boolean isHighlight) {
        int number = 8;
        ChessPosition redrawChessPosition;
        if (isHighlight) {
            redrawChessPosition = getMoveFromPlayer(isWhite);
            printHeading(out, isWhite);
            Collection<ChessMove> validMovesRedraw =  Client.lastChessGame.validMoves(redrawChessPosition);

            Set<ChessPosition> validPositions = new HashSet<>();
            for (ChessMove cp: validMovesRedraw){
                ChessPosition newEndPosistion = new ChessPosition(cp.getEndPosition().getRow() - 1, cp.getEndPosition().getColumn() - 1);
                validPositions.add(newEndPosistion);
            }

            for (int row = 0; row < BOARD_SIZE; row++) {
                out.print(SET_TEXT_COLOR_BLACK);
                printNumbers(out, number, isWhite);

                for (int col = BOARD_SIZE - 1; col >= 0; col--) {
                    checkToPrintmove(out, board, validPositions, row, col);
                }
                out.print(SET_TEXT_COLOR_BLACK);
                printNumbers(out, number, isWhite);
                number = number - 1;

                out.print(RESET_BG_COLOR + "\n");
            }
        }
        else{
            printHeading(out, isWhite);
            for (int row = 0; row < BOARD_SIZE; row++) {
                out.print(SET_TEXT_COLOR_BLACK);
                printNumbers(out, number, isWhite);

                for (int col = BOARD_SIZE - 1; col >= 0; col--) {
                    if((row + col) % 2 != 0) {
                        printPieces(out, board, row , col , SET_BG_COLOR_LIGHT_GREY);
                    }
                    else{
                        printPieces(out, board, row , col , SET_BG_COLOR_DARK_GREY);

                    }
                }
                printNumHandler(out, number, isWhite);
            }

        }
    }

    private static void printNumHandler(PrintStream out, int number, boolean isWhite) {
        out.print(SET_TEXT_COLOR_BLACK);
        printNumbers(out, number, isWhite);

        out.print(RESET_BG_COLOR + "\n");
    }

    private static ChessBoard getBoard() {
        ChessBoard chessBoard = new ChessBoard();
        chessBoard.resetBoard();
        return chessBoard;
    }


    private static void printWhiteChessBoard(PrintStream out, ChessBoard board, boolean isWhite,boolean isHightlightMove) {
        int number = 8;
        ChessPosition redrawChessPosition;
        if (isHightlightMove) {
            redrawChessPosition = getMoveFromPlayer(isWhite);
            printHeading(out, isWhite);
            int numb = 6;
            Collection<ChessMove> validMovesRedraw =  Client.lastChessGame.validMoves(redrawChessPosition);

            Set<ChessPosition> validPositions = new HashSet<>();
            for (ChessMove cp: validMovesRedraw){
                ChessPosition newEndPosistion = new ChessPosition(cp.getEndPosition().getRow() - 1, cp.getEndPosition().getColumn() - 1);
                validPositions.add(newEndPosistion);
            }
            int hello = 1;

            for (int row = BOARD_SIZE - 1; row >= 0; row--) {
                out.print(SET_TEXT_COLOR_BLACK);
                printNumbers(out, number, isWhite);

                for (int col = 0; col < BOARD_SIZE; col++) {
                    checkToPrintmove(out, board, validPositions, row, col);
                }
                out.print(SET_TEXT_COLOR_BLACK);
                printNumbers(out, number, isWhite);
                number = number - 1;

                out.print(RESET_BG_COLOR + "\n");
            }
        }
        else{
            printHeading(out, isWhite);
            for (int row = BOARD_SIZE - 1; row >= 0; row--) {
                out.print(SET_TEXT_COLOR_BLACK);
                printNumbers(out, number, isWhite);

                for (int col = 0; col < BOARD_SIZE; col++) {
                    if((row + col) % 2 != 0) {
                        printPieces(out, board, row , col , SET_BG_COLOR_LIGHT_GREY);
                    }
                    int numbersHi = 4;
                    else{
                        printPieces(out, board, row , col , SET_BG_COLOR_DARK_GREY);

                    }
                }
                printNumHandler(out, number, isWhite);
            }
        }
    }

    private static void checkToPrintmove(PrintStream out, ChessBoard board, Set<ChessPosition> validPositions, int row, int col) {
        ChessPosition currentChessPosisiton = new ChessPosition(row, col);
        boolean isValidMove = validPositions.contains(currentChessPosisiton);

        if((row + col) % 2 != 0){
            if(isValidMove){
                printPieces(out, board, row , col , SET_BG_COLOR_YELLOW);
            }
            else{
                printPieces(out, board, row , col , SET_BG_COLOR_LIGHT_GREY);
            }
        }

        else{
            if(isValidMove){
                printPieces(out, board, row , col , SET_BG_COLOR_GREEN);
            }
            else{
                printPieces(out, board, row , col , SET_BG_COLOR_DARK_GREY);
            }
        }
    }


    private static ChessPosition getMoveFromPlayer(boolean isWhite) {
        System.out.println("Enter piece position <a2>");
        String clientResponse = scanner.nextLine().trim().toLowerCase();
        if (clientResponse.length() == 2){
            char colChar = clientResponse.charAt(0);
            char rowChar = clientResponse.charAt(1);

            int row = 0;
            int col = 0;
            switch (colChar){
                case 'a':
                    col = 1;
                    break;
                case 'b':
                    col = 2;
                    break;
                case 'c':
                    col = 3;
                    break;
                case 'd':
                    col = 4;
                    break;
                case 'e':
                    col = 5;
                    break;
                case 'f':
                    col = 6;
                    break;
                case 'g':
                    col = 7;
                    break;
                case 'h':
                    col = 8;
                    break;
            }

            if(isWhite){
                row = Character.getNumericValue(rowChar);
            }
            else{
                row = Character.getNumericValue(rowChar);
//                row = 9 - row;
            }
            return new ChessPosition(row, col);
        }
        return new ChessPosition(-1, -1);
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

    private static void printNumbers(PrintStream out, int number, boolean isWhite) {
        out.print(SET_BG_COLOR_WHITE);
        if (isWhite) {
            out.print(" " + (number) + " ");
        } else {
            out.print(" " + (9 - number) + " ");
        }
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