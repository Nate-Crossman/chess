package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

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
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    private boolean isOnBoard(ChessPosition position) {
        if ((position.getRow() < 1) | (8 < position.getRow())) {
            return false;
        }
        if ((position.getColumn() < 1) | (8 < position.getColumn())) {
            return false;
        }
        return true;
    }

    private boolean isPositionEmpty(ChessBoard board, ChessPosition position) {
        return (board.getPiece(position) == null);
    }

    private boolean isPositionEnemy(ChessBoard board, ChessPosition position) {
        if (isPositionEmpty(board,position)) {
            return false;
        }
        ChessGame.TeamColor otherColor = board.getPiece(position).getTeamColor();
        if (pieceColor == ChessGame.TeamColor.WHITE && otherColor == ChessGame.TeamColor.BLACK) {
            return true;
        }
        if (pieceColor == ChessGame.TeamColor.BLACK && otherColor == ChessGame.TeamColor.WHITE) {
            return true;
        }
        return false;
    }

//    DOES NOT ACCOUNT FOR SPECIAL PAWN RULES OR CHECKS
    private boolean isPositionValidMove(ChessBoard board, ChessPosition position) {
        return (isOnBoard(position) && (isPositionEmpty(board, position) | isPositionEnemy(board, position)));
    }



    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch (type) {
            case KING -> {
                return getKingMoves(board, myPosition);
            }
            case KNIGHT -> {
                return getKnightMoves(board, myPosition);
            }
            case ROOK -> {
                return getRookMoves(board, myPosition);
            }
            case BISHOP -> {
                return getBishopMoves(board, myPosition);
            }
            case QUEEN -> {
                return getQueenMoves(board, myPosition);
            }
            case PAWN -> {
                return getPawnMoves(board, myPosition);
            }
            case null, default -> {return null;}
        }
    }

    private Collection<ChessMove> getKingMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> output = new HashSet<>();
        if (isPositionValidMove(board, myPosition.getNorthPostion())) {
            output.add(new ChessMove(myPosition, myPosition.getNorthPostion(), null));
        }
        if (isPositionValidMove(board, myPosition.getNorthWestPostion())) {
            output.add(new ChessMove(myPosition, myPosition.getNorthWestPostion(), null));
        }
        if (isPositionValidMove(board, myPosition.getWestPostion())) {
            output.add(new ChessMove(myPosition, myPosition.getWestPostion(), null));
        }
        if (isPositionValidMove(board, myPosition.getSouthWestPostion())) {
            output.add(new ChessMove(myPosition, myPosition.getSouthWestPostion(), null));
        }
        if (isPositionValidMove(board, myPosition.getSouthPostion())) {
            output.add(new ChessMove(myPosition, myPosition.getSouthPostion(), null));
        }
        if (isPositionValidMove(board, myPosition.getSouthEastPostion())) {
            output.add(new ChessMove(myPosition, myPosition.getSouthEastPostion(), null));
        }
        if (isPositionValidMove(board, myPosition.getEastPostion())) {
            output.add(new ChessMove(myPosition, myPosition.getEastPostion(), null));
        }
        if (isPositionValidMove(board, myPosition.getNorthEastPostion())) {
            output.add(new ChessMove(myPosition, myPosition.getNorthEastPostion(), null));
        }
        return output;
    }

    private Collection<ChessMove> getKnightMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> output = new HashSet<>();
        return output;
    }

    private Collection<ChessMove> getRookMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> output = new HashSet<>();
        return output;
    }

    private Collection<ChessMove> getBishopMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> output = new HashSet<>();
        return output;
    }

    private Collection<ChessMove> getQueenMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> output = new HashSet<>();
        return output;
    }

    private Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> output = new HashSet<>();
        return output;
    }

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

    @Override
    public String toString() {
        String output;
        switch (type) {
            case KING -> output = "k";
            case QUEEN -> output = "q";
            case BISHOP -> output = "b";
            case KNIGHT -> output = "h"; // h stands for horse
            case ROOK -> output = "r";
            case PAWN -> output = "p";
            case null, default -> output = " ";
        }
        return output;
    }
}
