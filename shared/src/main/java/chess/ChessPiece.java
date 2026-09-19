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

    private boolean isPositionAlly(ChessBoard board, ChessPosition position) {
        if (!isOnBoard(position)) {
            return false;
        }
        return (!isPositionEmpty(board, position) && !isPositionEnemy(board, position));
    }


//    DOES NOT ACCOUNT FOR SPECIAL PAWN RULES OR CHECKS
    private boolean isPositionValidMove(ChessBoard board, ChessPosition position) {
        return (isOnBoard(position) && (isPositionEmpty(board, position) | isPositionEnemy(board, position)));
    }

    //Rec Move Handler checks if movement can continue and adds the new position to output if possible
    private boolean recMoveHandler(ChessBoard board,
                                   ChessPosition start,
                                   ChessPosition newPosition,
                                   Collection<ChessMove> output) {
        // stop and do not add new position
        if (!isOnBoard(newPosition) | isPositionAlly(board, newPosition)) {
            return false;
        }
        // add new position to output but do not continue
        if (isPositionEnemy(board, newPosition)) {
            output.add(new ChessMove(start, newPosition, null));
            return false;
        }
        // add position and proceed
        output.add(new ChessMove(start, newPosition, null));
        return true;
    }

    private void recMoveNorth(ChessBoard board,
                              ChessPosition start,
                              ChessPosition position,
                              Collection<ChessMove> output) {
        ChessPosition newPosition = position.getNorthPostion();
        if (recMoveHandler(board, start, newPosition, output)) {
            recMoveNorth(board, start, newPosition, output);
        }
    }

    private void recMoveNorthEast(ChessBoard board,
                              ChessPosition start,
                              ChessPosition position,
                              Collection<ChessMove> output) {
        ChessPosition newPosition = position.getNorthEastPostion();
        if (recMoveHandler(board, start, newPosition, output)) {
            recMoveNorthEast(board, start, newPosition, output);
        }
    }

    private void recMoveEast(ChessBoard board,
                              ChessPosition start,
                              ChessPosition position,
                              Collection<ChessMove> output) {
        ChessPosition newPosition = position.getEastPostion();
        if (recMoveHandler(board, start, newPosition, output)) {
            recMoveEast(board, start, newPosition, output);
        }
    }

    private void recMoveSouthEast(ChessBoard board,
                              ChessPosition start,
                              ChessPosition position,
                              Collection<ChessMove> output) {
        ChessPosition newPosition = position.getSouthEastPostion();
        if (recMoveHandler(board, start, newPosition, output)) {
            recMoveSouthEast(board, start, newPosition, output);
        }
    }

    private void recMoveSouth(ChessBoard board,
                              ChessPosition start,
                              ChessPosition position,
                              Collection<ChessMove> output) {
        ChessPosition newPosition = position.getSouthPostion();
        if (recMoveHandler(board, start, newPosition, output)) {
            recMoveSouth(board, start, newPosition, output);
        }
    }

    private void recMoveSouthWest(ChessBoard board,
                              ChessPosition start,
                              ChessPosition position,
                              Collection<ChessMove> output) {
        ChessPosition newPosition = position.getSouthWestPostion();
        if (recMoveHandler(board, start, newPosition, output)) {
            recMoveSouthWest(board, start, newPosition, output);
        }
    }

    private void recMoveWest(ChessBoard board,
                              ChessPosition start,
                              ChessPosition position,
                              Collection<ChessMove> output) {
        ChessPosition newPosition = position.getWestPostion();
        if (recMoveHandler(board, start, newPosition, output)) {
            recMoveWest(board, start, newPosition, output);
        }
    }

    private void recMoveNorthWest(ChessBoard board,
                              ChessPosition start,
                              ChessPosition position,
                              Collection<ChessMove> output) {
        ChessPosition newPosition = position.getNorthWestPostion();
        if (recMoveHandler(board, start, newPosition, output)) {
            recMoveNorthWest(board, start, newPosition, output);
        }
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
        if (isPositionValidMove(board, myPosition.getUpUpRightPostion())) {
            output.add(new ChessMove(myPosition, myPosition.getUpUpRightPostion(), null));
        }
        if (isPositionValidMove(board, myPosition.getUpRightRightPostion())) {
            output.add(new ChessMove(myPosition, myPosition.getUpRightRightPostion(), null));
        }
        if (isPositionValidMove(board, myPosition.getDownRightRighthPostion())) {
            output.add(new ChessMove(myPosition, myPosition.getDownRightRighthPostion(), null));
        }
        if (isPositionValidMove(board, myPosition.getDownDownRightPostion())) {
            output.add(new ChessMove(myPosition, myPosition.getDownDownRightPostion(), null));
        }
        if (isPositionValidMove(board, myPosition.getDownDownLeftPostion())) {
            output.add(new ChessMove(myPosition, myPosition.getDownDownLeftPostion(), null));
        }
        if (isPositionValidMove(board, myPosition.getDownLeftLeftPostion())) {
            output.add(new ChessMove(myPosition, myPosition.getDownLeftLeftPostion(), null));
        }
        if (isPositionValidMove(board, myPosition.getUpLeftLeftPostion())) {
            output.add(new ChessMove(myPosition, myPosition.getUpLeftLeftPostion(), null));
        }
        if (isPositionValidMove(board, myPosition.getUpUpLeftPostion())) {
            output.add(new ChessMove(myPosition, myPosition.getUpUpLeftPostion(), null));
        }
        return output;
    }

    private Collection<ChessMove> getRookMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> output = new HashSet<>();
        recMoveNorth(board, myPosition, myPosition, output);
        recMoveWest(board, myPosition, myPosition, output);
        recMoveSouth(board, myPosition, myPosition, output);
        recMoveEast(board, myPosition, myPosition, output);
        return output;
    }

    private Collection<ChessMove> getBishopMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> output = new HashSet<>();
        recMoveNorthWest(board, myPosition, myPosition, output);
        recMoveSouthWest(board, myPosition, myPosition, output);
        recMoveSouthEast(board, myPosition, myPosition, output);
        recMoveNorthEast(board, myPosition, myPosition, output);
        return output;
    }

    private Collection<ChessMove> getQueenMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> output = new HashSet<>();
        recMoveNorth(board, myPosition, myPosition, output);
        recMoveWest(board, myPosition, myPosition, output);
        recMoveSouth(board, myPosition, myPosition, output);
        recMoveEast(board, myPosition, myPosition, output);
        recMoveNorthWest(board, myPosition, myPosition, output);
        recMoveSouthWest(board, myPosition, myPosition, output);
        recMoveSouthEast(board, myPosition, myPosition, output);
        recMoveNorthEast(board, myPosition, myPosition, output);
        return output;
    }

    private Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> output = new HashSet<>();
        int promotionRow;
        int doubleRow;
        ChessPosition diagonal1;
        ChessPosition diagonal2;
        ChessPosition doubleMove;
        ChessPosition regularMove;
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            promotionRow = 7;
            doubleRow = 2;
            diagonal1 = myPosition.getNorthEastPostion();
            diagonal2 = myPosition.getNorthWestPostion();
            doubleMove = myPosition.getNorthPostion().getNorthPostion();
            regularMove = myPosition.getNorthPostion();
        } else {
            promotionRow = 2;
            doubleRow = 7;
            diagonal1 = myPosition.getSouthEastPostion();
            diagonal2 = myPosition.getSouthWestPostion();
            doubleMove = myPosition.getSouthPostion().getSouthPostion();
            regularMove = myPosition.getSouthPostion();
        }

        if (isOnBoard(regularMove) && isPositionEmpty(board, regularMove)) {
            output.add(new ChessMove(myPosition, regularMove, null));
        }

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
