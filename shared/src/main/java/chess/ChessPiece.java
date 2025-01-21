package chess;

import chess.movesCalulator.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        PieceMovesCalculator calculator;
        switch (type){
            case PAWN :
                calculator = new PawnMovesCalculator();
                break;
            case ROOK :
                calculator = new RookMovesCalculator();
                break;
            case BISHOP :
                calculator = new BishopMovesCalculator();
                break;
            case KNIGHT :
                calculator = new KnightMovesCalculator();
                break;
            case KING :
                calculator = new KingMovesCalculator();
                break;
            default:
                throw new RuntimeException();
        }
        return calculator.pieceMoves(board, myPosition);


//        Collection<ChessMove> moves = new ArrayList<>();
//
//        if (type == PieceType.PAWN){
//            pawnMovesCalculator(board, myPosition, moves);
//        }
//        if (type == PieceType.BISHOP){
//            bishopMovesCalculator(board, myPosition, moves);
//        }
//        if (type == PieceType.KNIGHT){
//            knightMovesCalculator(board, myPosition, moves);
//        }
//        if (type == PieceType.ROOK){
//            rookMovesCalculator(board, myPosition, moves);
//        }
//
//
//
//        return moves;
    }

    private void pawnMovesCalculator(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves){
        int direction;
        int promotionRow;

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
            if (myPosition.getRow() >= 1 && myPosition.getRow() <= 8 && myPosition.getColumn() >= 1 && myPosition.getColumn() <= 8){
                if (oneStepForward.getRow() == promotionRow) {
                    for (ChessPiece.PieceType promotion : ChessPiece.PieceType.values()) {
                        if (PieceType.PAWN != promotion && PieceType.KING != promotion) {
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
            if (myPosition.getRow() >= 1 && myPosition.getRow() <= 8 && myPosition.getColumn() >= 1 && myPosition.getColumn() <= 8){

                if(pieceColor == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2 || pieceColor == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7) {
                    ChessPosition twoStepForward = new ChessPosition(myPosition.getRow() + direction * 2, myPosition.getColumn());
                    moves.add(new ChessMove(myPosition, twoStepForward, null));
                }
            }
        }
//        capture a piece if adjacent
        int[] captureColumns = {-1,1};
        for (int adjacent : captureColumns){
            ChessPosition capture = new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn() + adjacent);
//            checks for valid move
            if (myPosition.getRow() >= 1 && myPosition.getRow() <= 8 && myPosition.getColumn() >= 1 && myPosition.getColumn() <= 8){
                ChessPiece enemy = board.getPiece(capture);
                if (enemy != null && enemy.getTeamColor() != pieceColor){
                    if (capture.getRow() == promotionRow){
                        for (ChessPiece.PieceType promotion : ChessPiece.PieceType.values()){
                            if (PieceType.PAWN  != promotion && PieceType.KING != promotion){
                                moves.add(new ChessMove(myPosition, capture, promotion));
                            }
                        }
                    }
                    else{
                        moves.add(new ChessMove(myPosition, capture, null));}
                }
            }
        }

    }
    private void rookMovesCalculator(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves) {
        int [][] directions = {{1,0},{0,1},{-1,0},{0,-1}};
        for (int[] direction : directions){
            int row = myPosition.getRow();
            int col = myPosition.getColumn();

            while (true) {
                row += direction[0];
                col += direction[1];

                if (row < 1 || row >= 8 || col < 1 || col >= 8) {
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
    }

    private void knightMovesCalculator(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves) {

    }

    private void bishopMovesCalculator(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves) {

    }
}
