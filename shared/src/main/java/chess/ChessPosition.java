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

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return this.col;
    }

    // Intellj generated equals and hash codes
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
        char rowChar = ' '; //invalid coords will be represented by spaces
        char colChar = ' ';
        if (row >= 0 && row < 8) {
            rowChar = (char) (row+1);
        }
        if (col == 0) {colChar = 'a';}
        else if (col == 1) {colChar = 'b';}
        else if (col == 2) {colChar = 'c';}
        else if (col == 3) {colChar = 'd';}
        else if (col == 4) {colChar = 'e';}
        else if (col == 5) {colChar = 'f';}
        else if (col == 6) {colChar = 'g';}
        else if (col == 7) {colChar = 'h';}

        return String.format("{%c,%c}", rowChar, colChar);
    }
}
