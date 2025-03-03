package chess.movescalculator;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public interface PieceMovesCalculator {
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);

    default Collection<ChessMove> kingKnightMovesCalc(ChessBoard board, ChessPosition myPosition, int[][] directions) {
        Collection<ChessMove> moves = new ArrayList<>();
        for (int[] direction : directions) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            ChessGame.TeamColor pieceColor = board.getPiece(myPosition).getTeamColor();

            row += direction[0];
            col += direction[1];

            if (row >= 1 && row <= 8 && col >= 1 && col <= 8) {
                ChessPosition newPosition = new ChessPosition(row, col);
                ChessPiece pieceAtNewPosition = board.getPiece(newPosition);
                if (pieceAtNewPosition == null) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
                else if (pieceAtNewPosition.getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }

        }
        return moves;
    }


    default Collection<ChessMove> infiniteMoves(ChessBoard board, ChessPosition myPosition, int[][] directions) {
        Collection<ChessMove> moves = new ArrayList<>();
        for (int[] direction : directions){
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            ChessGame.TeamColor pieceColor = board.getPiece(myPosition).getTeamColor();

            while (true) {
                row += direction[0];
                col += direction[1];

                if (row < 1 || row >= 9 || col < 1 || col >= 9) {
                    break;
                }

                ChessPosition newPosition = new ChessPosition(row,col);
                ChessPiece pieceAtNewPosition = board.getPiece(newPosition);

                if (pieceAtNewPosition == null){
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
                else if (pieceAtNewPosition.getTeamColor() != pieceColor){
                    moves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                }
                else{
                    break;
                }
            }
        }
        return moves;
    }
}
