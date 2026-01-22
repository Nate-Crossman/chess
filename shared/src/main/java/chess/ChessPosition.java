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

    public ChessPosition getNorthPosition() {
        return new ChessPosition(row+1,col);
    }
    public ChessPosition getNorthEastPosition() {
        return new ChessPosition(row+1,col+1);
    }
    public ChessPosition getEastPosition() {
        return new ChessPosition(row,col+1);
    }
    public ChessPosition getSouthEastPosition() {
        return new ChessPosition(row-1,col+1);
    }
    public ChessPosition getSouthPosition() {
        return new ChessPosition(row-1,col);
    }
    public ChessPosition getSouthWestPosition() {
        return new ChessPosition(row-1,col-1);
    }
    public ChessPosition getWestPosition() {
        return new ChessPosition(row,col-1);
    }
    public ChessPosition getNorthWestPosition() {
        return new ChessPosition(row+1,col-1);
    }

    //knight moves
    public ChessPosition getUpUpRightPosition() {
        return new ChessPosition(row+2, col+1);
    }
    public ChessPosition getUpRightRightPosition() {
        return new ChessPosition(row+1, col+2);
    }
    public ChessPosition getDownRightRightPosition() {
        return new ChessPosition(row-1, col+2);
    }
    public ChessPosition getDownDownRightPosition() {
        return new ChessPosition(row-2, col+1);
    }
    public ChessPosition getDownDownLeftPosition() {
        return new ChessPosition(row-2, col-1);
    }
    public ChessPosition getDownLeftLeftPosition() {
        return new ChessPosition(row-1, col-2);
    }
    public ChessPosition getUpLeftLeftPosition() {
        return new ChessPosition(row+1, col-2);
    }
    public ChessPosition getUpUpLeftPosition() {
        return new ChessPosition(row+2, col-1);
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
        //invalid coords will be represented by spaces
        char colChar = ' ';
        if (col == 1) {colChar = 'a';}
        else if (col == 2) {colChar = 'b';}
        else if (col == 3) {colChar = 'c';}
        else if (col == 4) {colChar = 'd';}
        else if (col == 5) {colChar = 'e';}
        else if (col == 6) {colChar = 'f';}
        else if (col == 7) {colChar = 'g';}
        else if (col == 8) {colChar = 'h';}

        return String.format("{%d,%c}", row, colChar);
    }
}
