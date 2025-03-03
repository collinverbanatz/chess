package chess.movescalculator;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
    int [][] directions = {{1,0},{0,1},{-1,0},{0,-1}};
    return infiniteMoves(board, myPosition, directions);

    }
}
