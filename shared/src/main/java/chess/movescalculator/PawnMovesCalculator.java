package chess.movescalculator;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int direction;
        int promotionRow;
        ChessGame.TeamColor pieceColor = board.getPiece(myPosition).getTeamColor();
        Collection<ChessMove> moves = new ArrayList<>();


        if (pieceColor == ChessGame.TeamColor.WHITE){
            direction = 1;
            promotionRow = 8;
        }
        else{
            direction = -1;
            promotionRow = 1;
        }

//        makes pawn move one step forward
        ChessPosition oneStepForward = new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn());
        ChessPiece team = board.getPiece(oneStepForward);
        if (team == null) {
            if (myPosition.getRow() >= 1 && myPosition.getRow() <= 7 && myPosition.getColumn() >= 1 && myPosition.getColumn() <= 8){
                if (oneStepForward.getRow() == promotionRow) {
                    for (ChessPiece.PieceType promotion : ChessPiece.PieceType.values()) {
                        if (ChessPiece.PieceType.PAWN != promotion && ChessPiece.PieceType.KING != promotion) {
                            moves.add(new ChessMove(myPosition, oneStepForward, promotion));
                        }
                    }
                }
                else {
                    moves.add(new ChessMove(myPosition, oneStepForward, null));
                }
            }
        }

//        makes pawn move 2 steps forward if it's at starting position
        if (team == null){

            if(pieceColor == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2 ||
                    pieceColor == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7) {
                ChessPosition twoStepForward = new ChessPosition(myPosition.getRow() + direction * 2, myPosition.getColumn());
                if (board.getPiece(twoStepForward) == null && board.getPiece(oneStepForward) == null) {
                    moves.add(new ChessMove(myPosition, twoStepForward, null));
                }
            }

        }
//        capture a piece if adjacent
        int[] captureColumns = {-1,1};
        for (int adjacent : captureColumns){
            ChessPosition capture = new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn() + adjacent);
//            checks for valid move
            if (myPosition.getRow() >= 1 && myPosition.getRow() <= 7 && myPosition.getColumn() >= 1 && myPosition.getColumn() <= 8){
                ChessPiece enemy = board.getPiece(capture);
                if (enemy != null && enemy.getTeamColor() != pieceColor){
                    if (capture.getRow() == promotionRow){
                        for (ChessPiece.PieceType promotion : ChessPiece.PieceType.values()){
                            if (ChessPiece.PieceType.PAWN  != promotion && ChessPiece.PieceType.KING != promotion){
                                moves.add(new ChessMove(myPosition, capture, promotion));
                            }
                        }
                    }
                    else{
                        moves.add(new ChessMove(myPosition, capture, null));}
                }
            }
        }


        return moves;
    }
}
