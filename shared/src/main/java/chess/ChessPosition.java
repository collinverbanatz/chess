package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */

public class ChessPosition {
    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static ChessPosition fromPosition(String position){
        if (!isValidPosition(position)) {
            throw new RuntimeException("Got invalid position");
        }
        int row = position.charAt(1) - '1' + 1;
        int col = position.charAt(0) - 'a' + 1;
        return new ChessPosition(row, col);
    }

    public static boolean isValidPosition(String position) {
        if (position.length() != 2) {
            return false;
        }
        if (position.charAt(0) < 'a' || position.charAt(0) > 'h') {
            return false;
        }
        if (position.charAt(1) < '1' || position.charAt(1) > '8') {
            return false;
        }
        return true;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left col
     */
    public int getColumn() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "ChessPosition{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
}
