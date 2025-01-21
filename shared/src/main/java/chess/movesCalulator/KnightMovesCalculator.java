package chess.movesCalulator;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnightMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int[][] directions = {{1, 2}, {1, -2}, {-1, 2}, {-1, -2}, {2, 1}, {2, -1}, {-2, 1}, {-2, -1}};
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
}