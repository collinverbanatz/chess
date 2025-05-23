package chess;

import chess.movescalculator.*;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess pieces
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

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
            case QUEEN :
                calculator = new QueenMovesCalculator();
                break;
            default:
                throw new RuntimeException();
        }
        return calculator.pieceMoves(board, myPosition);

    }
    @Override
    public String toString() {
        String pieceSymbol = switch (type) {
            case KING -> "K";
            case QUEEN -> "Q";
            case ROOK -> "R";
            case BISHOP -> "B";
            case KNIGHT -> "N";
            case PAWN -> "P";
        };
        return pieceColor == ChessGame.TeamColor.WHITE ? pieceSymbol : pieceSymbol.toLowerCase();
    }
}
