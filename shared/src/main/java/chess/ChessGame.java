package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor currentColor;

    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard();
        this.currentColor = TeamColor.WHITE;

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentColor;
//        throw new RuntimeException("Not implemented");
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.currentColor = team;
//        throw new RuntimeException("Not implemented");
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if( piece == null){
            return null;
        }

//        get current pieces endpositions and see if you're in check implementation: move piece temporarily and then check to see if in check
        Collection<ChessMove> legalMoves = new HashSet<>();
        for (ChessMove move : piece.pieceMoves(board, startPosition)){
            legalMoves.add(move);
        }
        for(ChessMove posibleMove : legalMoves){
            ChessPiece tempPiece = board.getPiece(posibleMove.getEndPosition());
            board.addPiece(startPosition, null);
            board.addPiece(posibleMove.getEndPosition(), piece);

        }


        return piece.pieceMoves(board, startPosition);
//        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented hello world");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public ChessPosition findKing(TeamColor teamColor){
        ChessPosition kingPos = null;
        for (int row = 1; row <= 8; row++){
            for (int col = 1; col <= 8; col++) {
                ChessPosition newPos = new ChessPosition(row, col);
                ChessPiece newPiece = board.getPiece(newPos);
                if(newPiece != null && newPiece.getTeamColor() == teamColor && newPiece.getPieceType() == ChessPiece.PieceType.KING){
                    kingPos = newPos;
                }
            }
        }
        return kingPos;
    }

    public boolean isInCheck(TeamColor teamColor) {
//        find the kings position
        ChessPosition kingPos = findKing(teamColor);
//        for (int row = 1; row <= 8; row++){
//            for (int col = 1; col <= 8; col++) {
//                ChessPosition newPos = new ChessPosition(row, col);
//                ChessPiece newPiece = board.getPiece(newPos);
//                if(newPiece != null && newPiece.getTeamColor() == teamColor && newPiece.getPieceType() == ChessPiece.PieceType.KING){
//                    kingPos = newPos;
//                }
//            }
//        }

//        see if the king is in check by looping through each enemy piece to see if it can attack where the kingPos is
        for(int row = 1; row <= 8; row++){
            for (int col = 1; col <= 8; col++){
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
//                check to see if it's enemy piece and it's not empty(there is a piece there)
                if(piece != null && piece.getTeamColor() != teamColor){
//                    loop through each of the enemies moves to see if it can attack our king
                    for(ChessMove move : piece.pieceMoves(board, position)){
                        if(move.getEndPosition().equals(kingPos)){
                            return true;
                        }
                    }
                }
            }
        }

//        throw new RuntimeException("Not implemented");
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {

        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
//        checks to see if in check
        if(isInCheck(teamColor)){
            return false;
        }

        //        check to see if king has no available move to make that don't put it in check
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if(piece != null && teamColor == piece.getTeamColor()){
                    for(ChessMove move : piece.pieceMoves(board, position)) {
                        if (move.getEndPosition() != null) {
                            return false;
                        }
                    }
                }
            }
        }

//        throw new RuntimeException("Not implemented");
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
//        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
//        throw new RuntimeException("Not implemented");
    }
}
