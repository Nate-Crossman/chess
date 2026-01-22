package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

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
        if (position.getRow() < 0 | position.getRow() > 7) {
            return false;
        }
        if (position.getColumn() < 0 | position.getColumn() > 7) {
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
        return true;
    }

    // returns a collection of every possible move of a King Piece on a position
    private Collection<ChessMove> getKingMove(ChessBoard board, ChessPosition myPosition) {
        return null;
    }

    // returns a collection of every possible move of a Knight Piece on a position
    private Collection<ChessMove> getKnightMove(ChessBoard board, ChessPosition myPosition) {
        return null;
    }

    // returns a collection of every possible move of a Rook Piece on a position
    private Collection<ChessMove> getRookMove(ChessBoard board, ChessPosition myPosition) {
        return null;
    }

    // returns a collection of every possible move of a Bishop Piece on a position
    private Collection<ChessMove> getBishopMove(ChessBoard board, ChessPosition myPosition) {
        return null;
    }

    // returns a collection of every possible move of a Queen Piece on a position
    private Collection<ChessMove> getQueenMove(ChessBoard board, ChessPosition myPosition) {
        return null;
    }

    // returns a collection of every possible move of a Black Pawn Piece on a position
    private Collection<ChessMove> getPawnMoveBlack(ChessBoard board, ChessPosition myPosition) {
        return null;
    }

    // returns a collection of every possible move of a White Pawn Piece on a position
    private Collection<ChessMove> getPawnMoveWhite(ChessBoard board, ChessPosition myPosition) {
        return null;
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

        }
        //knight moves
        else if (type == PieceType.KNIGHT) {

        }
        //rook moves
        else if (type == PieceType.ROOK) {

        }

        //bishop moves
        else if (type == PieceType.BISHOP) {}
        //queen moves
        else if (type == PieceType.QUEEN) {}
        //pawn moves
        else if (type == PieceType.PAWN) {

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
}
