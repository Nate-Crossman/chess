package chess;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece implements Cloneable {

    ChessGame.TeamColor color;
    ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType pieceType) {
        color = pieceColor;
        type = pieceType;
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
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /// HELPER FUNCTIONS ///

    // returns true if a position is on the chess board
    private boolean validPosition (ChessPosition position) {
        if (position.getRow() < 1 | position.getRow() > 8) {
            return false;
        }
        if (position.getColumn() < 1 | position.getColumn() > 8) {
            return false;
        }
        return true;
    }

    // returns true if a position has a piece on it
    private boolean containsPiece(ChessBoard board, ChessPosition position) {
        return (board.getPiece(position) != null);
    }

    // returns true if the piece on the position is the opposite color as this piece
    private boolean isEnemy(ChessBoard board, ChessPosition position) {
        ChessGame.TeamColor otherType = board.getPiece(position).getTeamColor();
        if (color == ChessGame.TeamColor.WHITE && otherType == ChessGame.TeamColor.BLACK) {
            return true;
        }
        if (color == ChessGame.TeamColor.BLACK && otherType == ChessGame.TeamColor.WHITE) {
            return true;
        }
        return false;
    }

    // returns true if the position is open for regular movement (empty or enemy piece)
    private boolean validMovePosition(ChessBoard board, ChessPosition position) {
        if (!validPosition(position)) return false;
        if (!containsPiece(board, position)) return true;
        if (isEnemy(board,position)) return true;
        return false;
    }

    /// RECURSIVE MOVE FUNCTIONS FOR ROOKS, BISHOPS, and QUEENS
    /// If I could figure out how to put a method into an argument, this could be 1 function.
    //recursive function that moves North until it cannot
    private void recMoveNorth(ChessBoard board, ChessPosition start, ChessPosition position, Collection<ChessMove> output) {
        ChessPosition newPosition = position.getNorthPosition();
        //north position is out of bounds
        if (!validPosition(newPosition)) return;
        //north position is ally
        if (containsPiece(board, newPosition) && !isEnemy(board, newPosition)) return;
        //north position is enemy
        if (containsPiece(board, newPosition) && isEnemy(board, newPosition)) {
            output.add(new ChessMove(start, newPosition, null));
            return;
        }
        output.add(new ChessMove(start, newPosition, null));
        recMoveNorth(board, start, newPosition, output);
    }

    //recursive function that moves NorthWest until it cannot
    private void recMoveNorthWest(ChessBoard board, ChessPosition start, ChessPosition position, Collection<ChessMove> output) {
        ChessPosition newPosition = position.getNorthWestPosition();
        //north position is out of bounds
        if (!validPosition(newPosition)) return;
        //north position is ally
        if (containsPiece(board, newPosition) && !isEnemy(board, newPosition)) return;
        //north position is enemy
        if (containsPiece(board, newPosition) && isEnemy(board, newPosition)) {
            output.add(new ChessMove(start, newPosition, null));
            return;
        }
        output.add(new ChessMove(start, newPosition, null));
        recMoveNorthWest(board, start, newPosition, output);
    }

    //recursive function that moves West until it cannot
    private void recMoveWest(ChessBoard board, ChessPosition start, ChessPosition position, Collection<ChessMove> output) {
        ChessPosition newPosition = position.getWestPosition();
        //north position is out of bounds
        if (!validPosition(newPosition)) return;
        //north position is ally
        if (containsPiece(board, newPosition) && !isEnemy(board, newPosition)) return;
        //north position is enemy
        if (containsPiece(board, newPosition) && isEnemy(board, newPosition)) {
            output.add(new ChessMove(start, newPosition, null));
            return;
        }
        output.add(new ChessMove(start, newPosition, null));
        recMoveWest(board, start, newPosition, output);
    }

    //recursive function that moves Southwest until it cannot
    private void recMoveSouthWest(ChessBoard board, ChessPosition start, ChessPosition position, Collection<ChessMove> output) {
        ChessPosition newPosition = position.getSouthWestPosition();
        //north position is out of bounds
        if (!validPosition(newPosition)) return;
        //north position is ally
        if (containsPiece(board, newPosition) && !isEnemy(board, newPosition)) return;
        //north position is enemy
        if (containsPiece(board, newPosition) && isEnemy(board, newPosition)) {
            output.add(new ChessMove(start, newPosition, null));
            return;
        }
        output.add(new ChessMove(start, newPosition, null));
        recMoveSouthWest(board, start, newPosition, output);
    }

    //recursive function that moves South until it cannot
    private void recMoveSouth(ChessBoard board, ChessPosition start, ChessPosition position, Collection<ChessMove> output) {
        ChessPosition newPosition = position.getSouthPosition();
        //north position is out of bounds
        if (!validPosition(newPosition)) return;
        //north position is ally
        if (containsPiece(board, newPosition) && !isEnemy(board, newPosition)) return;
        //north position is enemy
        if (containsPiece(board, newPosition) && isEnemy(board, newPosition)) {
            output.add(new ChessMove(start, newPosition, null));
            return;
        }
        output.add(new ChessMove(start, newPosition, null));
        recMoveSouth(board, start, newPosition, output);
    }

    //recursive function that moves Southeast until it cannot
    private void recMoveSouthEast(ChessBoard board, ChessPosition start, ChessPosition position, Collection<ChessMove> output) {
        ChessPosition newPosition = position.getSouthEastPosition();
        //north position is out of bounds
        if (!validPosition(newPosition)) return;
        //north position is ally
        if (containsPiece(board, newPosition) && !isEnemy(board, newPosition)) return;
        //north position is enemy
        if (containsPiece(board, newPosition) && isEnemy(board, newPosition)) {
            output.add(new ChessMove(start, newPosition, null));
            return;
        }
        output.add(new ChessMove(start, newPosition, null));
        recMoveSouthEast(board, start, newPosition, output);
    }

    //recursive function that moves East until it cannot
    private void recMoveEast(ChessBoard board, ChessPosition start, ChessPosition position, Collection<ChessMove> output) {
        ChessPosition newPosition = position.getEastPosition();
        //north position is out of bounds
        if (!validPosition(newPosition)) return;
        //north position is ally
        if (containsPiece(board, newPosition) && !isEnemy(board, newPosition)) return;
        //north position is enemy
        if (containsPiece(board, newPosition) && isEnemy(board, newPosition)) {
            output.add(new ChessMove(start, newPosition, null));
            return;
        }
        output.add(new ChessMove(start, newPosition, null));
        recMoveEast(board, start, newPosition, output);
    }

    //recursive function that moves Northeast until it cannot
    private void recMoveNorthEast(ChessBoard board, ChessPosition start, ChessPosition position, Collection<ChessMove> output) {
        ChessPosition newPosition = position.getNorthEastPosition();
        //north position is out of bounds
        if (!validPosition(newPosition)) return;
        //north position is ally
        if (containsPiece(board, newPosition) && !isEnemy(board, newPosition)) return;
        //north position is enemy
        if (containsPiece(board, newPosition) && isEnemy(board, newPosition)) {
            output.add(new ChessMove(start, newPosition, null));
            return;
        }
        output.add(new ChessMove(start, newPosition, null));
        recMoveNorthEast(board, start, newPosition, output);
    }


    /// METHODS THAT RETURN THE COLLECTION OF MOVES, CALLED BASED ON PIECE TYPE

    // returns a collection of every possible move of a King Piece on a position
    private Collection<ChessMove> getKingMove(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> output = new HashSet<>();
        if (validMovePosition(board, myPosition.getNorthPosition())) {
            output.add(new ChessMove(myPosition, myPosition.getNorthPosition(), null));
        }
        if (validMovePosition(board, myPosition.getNorthWestPosition())) {
            output.add(new ChessMove(myPosition, myPosition.getNorthWestPosition(), null));
        }
        if (validMovePosition(board, myPosition.getWestPosition())) {
            output.add(new ChessMove(myPosition, myPosition.getWestPosition(), null));
        }
        if (validMovePosition(board, myPosition.getSouthWestPosition())) {
            output.add(new ChessMove(myPosition, myPosition.getSouthWestPosition(), null));
        }
        if (validMovePosition(board, myPosition.getSouthPosition())) {
            output.add(new ChessMove(myPosition, myPosition.getSouthPosition(), null));
        }
        if (validMovePosition(board, myPosition.getSouthEastPosition())) {
            output.add(new ChessMove(myPosition, myPosition.getSouthEastPosition(), null));
        }
        if (validMovePosition(board, myPosition.getEastPosition())) {
            output.add(new ChessMove(myPosition, myPosition.getEastPosition(), null));
        }
        if (validMovePosition(board, myPosition.getNorthEastPosition())) {
            output.add(new ChessMove(myPosition, myPosition.getNorthEastPosition(), null));
        }
        return output;
    }

    // returns a collection of every possible move of a Knight Piece on a position
    private Collection<ChessMove> getKnightMove(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> output = new HashSet<>();
        if (validMovePosition(board, myPosition.getUpUpRightPosition())) {
            output.add(new ChessMove(myPosition, myPosition.getUpUpRightPosition(), null));
        }
        if (validMovePosition(board, myPosition.getUpRightRightPosition())) {
            output.add(new ChessMove(myPosition, myPosition.getUpRightRightPosition(), null));
        }
        if (validMovePosition(board, myPosition.getDownRightRightPosition())) {
            output.add(new ChessMove(myPosition, myPosition.getDownRightRightPosition(), null));
        }
        if (validMovePosition(board, myPosition.getDownDownRightPosition())) {
            output.add(new ChessMove(myPosition, myPosition.getDownDownRightPosition(), null));
        }
        if (validMovePosition(board, myPosition.getDownDownLeftPosition())) {
            output.add(new ChessMove(myPosition, myPosition.getDownDownLeftPosition(), null));
        }
        if (validMovePosition(board, myPosition.getDownLeftLeftPosition())) {
            output.add(new ChessMove(myPosition, myPosition.getDownLeftLeftPosition(), null));
        }
        if (validMovePosition(board, myPosition.getUpLeftLeftPosition())) {
            output.add(new ChessMove(myPosition, myPosition.getUpLeftLeftPosition(), null));
        }
        if (validMovePosition(board, myPosition.getUpUpLeftPosition())) {
            output.add(new ChessMove(myPosition, myPosition.getUpUpLeftPosition(), null));
        }
        return output;
    }

    // returns a collection of every possible move of a Rook Piece on a position
    private Collection<ChessMove> getRookMove(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> output = new HashSet<>();
        recMoveNorth(board, myPosition, myPosition, output);
        recMoveEast(board, myPosition, myPosition, output);
        recMoveSouth(board, myPosition, myPosition, output);
        recMoveWest(board, myPosition, myPosition, output);
        return output;
    }

    // returns a collection of every possible move of a Bishop Piece on a position
    private Collection<ChessMove> getBishopMove(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> output = new HashSet<>();
        recMoveNorthWest(board, myPosition, myPosition, output);
        recMoveNorthEast(board, myPosition, myPosition, output);
        recMoveSouthEast(board, myPosition, myPosition, output);
        recMoveSouthWest(board, myPosition, myPosition, output);
        return output;
    }

    // returns a collection of every possible move of a Queen Piece on a position
    private Collection<ChessMove> getQueenMove(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> output = new HashSet<>();
        recMoveNorth(board, myPosition, myPosition, output);
        recMoveEast(board, myPosition, myPosition, output);
        recMoveSouth(board, myPosition, myPosition, output);
        recMoveWest(board, myPosition, myPosition, output);
        recMoveNorthWest(board, myPosition, myPosition, output);
        recMoveNorthEast(board, myPosition, myPosition, output);
        recMoveSouthEast(board, myPosition, myPosition, output);
        recMoveSouthWest(board, myPosition, myPosition, output);
        return output;
    }

    // returns a collection of every possible move of a White Pawn Piece on a position
    private Collection<ChessMove> getPawnMoveWhite(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> output = new HashSet<>();
        ChessPosition north = myPosition.getNorthPosition();
        ChessPosition northeast = myPosition.getNorthEastPosition();
        ChessPosition northwest = myPosition.getNorthWestPosition();
        //double move
        if (myPosition.getRow() == 2) {
            ChessPosition doubleNorth = myPosition.getDoubleNorthPosition();
            if (!containsPiece(board, doubleNorth) && !containsPiece(board, north)) {
                output.add(new ChessMove(myPosition, doubleNorth, null));
            }
        }
        //regular move

        if (!containsPiece(board, north)) {
            if (myPosition.getRow() == 7) {
                output.add(new ChessMove(myPosition, north, PieceType.ROOK));
                output.add(new ChessMove(myPosition, north, PieceType.BISHOP));
                output.add(new ChessMove(myPosition, north, PieceType.KNIGHT));
                output.add(new ChessMove(myPosition, north, PieceType.QUEEN));
            }
            else {output.add(new ChessMove(myPosition, north, null));}
        }
        //regular attack
        if (validPosition(northeast) && containsPiece(board,northeast) && isEnemy(board, northeast)) {
            if (myPosition.getRow() == 7) {
                output.add(new ChessMove(myPosition, northeast, PieceType.ROOK));
                output.add(new ChessMove(myPosition, northeast, PieceType.BISHOP));
                output.add(new ChessMove(myPosition, northeast, PieceType.KNIGHT));
                output.add(new ChessMove(myPosition, northeast, PieceType.QUEEN));
            } else output.add(new ChessMove(myPosition, northeast, null));
        }
        if (validPosition(northwest) && containsPiece(board, northwest) && isEnemy(board, northwest)) {
            if (myPosition.getRow() == 7) {
                output.add(new ChessMove(myPosition, northwest, PieceType.ROOK));
                output.add(new ChessMove(myPosition, northwest, PieceType.BISHOP));
                output.add(new ChessMove(myPosition, northwest, PieceType.KNIGHT));
                output.add(new ChessMove(myPosition, northwest, PieceType.QUEEN));
            } else output.add(new ChessMove(myPosition, northwest, null));
        }
        return output;
    }

    // returns a collection of every possible move of a Black Pawn Piece on a position
    private Collection<ChessMove> getPawnMoveBlack(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> output = new HashSet<>();
        ChessPosition south = myPosition.getSouthPosition();
        ChessPosition southeast = myPosition.getSouthEastPosition();
        ChessPosition southwest = myPosition.getSouthWestPosition();
        //double move
        if (myPosition.getRow() == 7) {
            ChessPosition doubleSouth = myPosition.getDoubleSouthPosition();
            if (!containsPiece(board, doubleSouth) && !containsPiece(board, south)) {
                output.add(new ChessMove(myPosition, doubleSouth, null));
            }
        }
        //regular move
        if (!containsPiece(board, south)) {
            if (myPosition.getRow() == 2) {
                output.add(new ChessMove(myPosition, south, PieceType.ROOK));
                output.add(new ChessMove(myPosition, south, PieceType.BISHOP));
                output.add(new ChessMove(myPosition, south, PieceType.KNIGHT));
                output.add(new ChessMove(myPosition, south, PieceType.QUEEN));
            }
            else {output.add(new ChessMove(myPosition, south, null));}
        }
        //regular attack
        if (validPosition(southeast) && containsPiece(board,southeast) && isEnemy(board, southeast)) {
            if (myPosition.getRow() == 2) {
                output.add(new ChessMove(myPosition, southeast, PieceType.ROOK));
                output.add(new ChessMove(myPosition, southeast, PieceType.BISHOP));
                output.add(new ChessMove(myPosition, southeast, PieceType.KNIGHT));
                output.add(new ChessMove(myPosition, southeast, PieceType.QUEEN));
            } else output.add(new ChessMove(myPosition, southeast, null));
        }
        if (validPosition(southwest) && containsPiece(board, southwest) && isEnemy(board, southwest)) {
            if (myPosition.getRow() == 2) {
                output.add(new ChessMove(myPosition, southwest, PieceType.ROOK));
                output.add(new ChessMove(myPosition, southwest, PieceType.BISHOP));
                output.add(new ChessMove(myPosition, southwest, PieceType.KNIGHT));
                output.add(new ChessMove(myPosition, southwest, PieceType.QUEEN));
            } else output.add(new ChessMove(myPosition, southwest, null));
        }
        return output;
    }



    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        //king moves
        if (type == PieceType.KING) {
            return getKingMove(board,myPosition);
        }
        //knight moves
        if (type == PieceType.KNIGHT) {
            return getKnightMove(board,myPosition);
        }
        //rook moves
        if (type == PieceType.ROOK) {
            return getRookMove(board, myPosition);
        }
        //bishop moves
        if (type == PieceType.BISHOP) {
            return getBishopMove(board, myPosition);
        }
        //queen moves
        if (type == PieceType.QUEEN) {
            return getQueenMove(board, myPosition);
        }
        //pawn moves
        if (type == PieceType.PAWN) {
            if (color == ChessGame.TeamColor.WHITE) return getPawnMoveWhite(board, myPosition);
            if (color == ChessGame.TeamColor.BLACK) return getPawnMoveBlack(board, myPosition);
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return color == that.color && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }

    @Override
    public String toString() {
        char output = ' ';
        String outputColor = "";
        String end = "";
        if (type == PieceType.PAWN) {output = 'P';}
        else if (type == PieceType.ROOK) {output = 'R';}
        else if (type == PieceType.KNIGHT) {output = 'H';} //knight is H for horse, K is king
        else if (type == PieceType.BISHOP) {output = 'B';}
        else if (type == PieceType.QUEEN) {output = 'Q';}
        else if (type == PieceType.KING) {output = 'K';}

//        if (color == ChessGame.TeamColor.WHITE) {
//            outputColor = "\u001B[34m"; //for visibility, white is blue, black is red
//        }
//        else if (color == ChessGame.TeamColor.BLACK) {
//            outputColor = "\u001B[31m";
//        }
//        if (outputColor != "") {
//            end = "\u001B[0m";
//        }
        return String.format("%s%c%s", outputColor, output, end);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
