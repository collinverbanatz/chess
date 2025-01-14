package chess;

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
        Collection<ChessMove> moves = new ArrayList<>();

        if (type == PieceType.PAWN){
            pawnMovesCalculator(board, myPosition, moves);
        }


        return moves;
    }

    private void pawnMovesCalculator(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves){
        int direction;
        if (pieceColor == ChessGame.TeamColor.WHITE){
            direction = 1;
        }
        else{
            direction = -1;
        }

        ChessPosition oneStepForward = new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn());
        moves.add(new ChessMove(myPosition, oneStepForward, null));

        if(pieceColor == ChessGame.TeamColor.WHITE && myPosition.getRow() == 7){
            moves.add(new ChessMove(myPosition, ))
        }
    }

}
