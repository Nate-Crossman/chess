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
     * 1 codes for the left column
     */
    public int getColumn() {
        return this.col;
    }

    //Position functions for piece movement

    public ChessPosition getNorthPostion() {
        return new ChessPosition(row+1,col);
    }

    public ChessPosition getNorthEastPostion() {
        return new ChessPosition(row+1,col+1);
    }

    public ChessPosition getEastPostion() {
        return new ChessPosition(row,col+1);
    }

    public ChessPosition getSouthEastPostion() {
        return new ChessPosition(row-1,col+1);
    }

    public ChessPosition getSouthPostion() {
        return new ChessPosition(row-1,col);
    }

    public ChessPosition getSouthWestPostion() {
        return new ChessPosition(row-1,col-1);
    }

    public ChessPosition getWestPostion() {
        return new ChessPosition(row,col-1);
    }

    public ChessPosition getNorthWestPostion() {
        return new ChessPosition(row+1,col-1);
    }

    public ChessPosition getUpUpRightPostion() {
        return new ChessPosition(row+2,col+1);
    }

    public ChessPosition getUpRightRightPostion() {
        return new ChessPosition(row+1,col+2);
    }

    public ChessPosition getDownRightRighthPostion() {
        return new ChessPosition(row-1,col+2);
    }

    public ChessPosition getDownDownRightPostion() {
        return new ChessPosition(row-2,col+1);
    }

    public ChessPosition getDownDownLeftPostion() {
        return new ChessPosition(row-2,col-1);
    }

    public ChessPosition getDownLeftLeftPostion() {
        return new ChessPosition(row-1,col-2);
    }

    public ChessPosition getUpLeftLeftPostion() {
        return new ChessPosition(row+1,col-2);
    }

    public ChessPosition getUpUpLeftPostion() {
        return new ChessPosition(row+2,col-1);
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
        return "(" + row + "," + col + ")";
    }
}
