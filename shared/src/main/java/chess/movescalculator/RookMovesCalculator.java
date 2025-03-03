package chess.movescalculator;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
    int [][] directions = {{1,0},{0,1},{-1,0},{0,-1}};
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
