package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard implements Cloneable {

    ChessPiece[][] tiles;
    public ChessBoard() {
        tiles =  new ChessPiece[8][8];
    }

    public ChessBoard(ChessBoard board) {
        this.tiles = new ChessPiece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPosition position = new ChessPosition(i+1,j+1);
                ChessPiece piece = board.getPiece(position);
                if (piece != null) {
                    this.tiles[i][j] = piece;
                }
            }
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        tiles[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */

    public ChessPiece getPiece(ChessPosition position) {
        return tiles[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //empty board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tiles[i][j] = null;
            }
        }
        //Note to self : White side is a1, black side is 8h
        //BLACK
        //black pawns
        for (int i = 0; i < 8; i++) {
            tiles[6][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
        //black backline
        tiles[7][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        tiles[7][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        tiles[7][2] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        tiles[7][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        tiles[7][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        tiles[7][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        tiles[7][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        tiles[7][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        //white pawns
        for (int i = 0; i < 8; i++) {
            tiles[1][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        }
        //white backline
        tiles[0][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        tiles[0][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        tiles[0][2] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        tiles[0][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        tiles[0][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        tiles[0][5] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        tiles[0][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        tiles[0][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
    }

    //Using a ChessMove, moves the piece from the start position to the end position.
    public void movePiece(ChessMove move) {
        ChessPiece piece = getPiece(move.getStartPosition());
        if (piece == null) return;
        if (move.getPromotionPiece() == null) {
            addPiece(move.getEndPosition(), piece);
        }
        else if (move.getPromotionPiece() == ChessPiece.PieceType.QUEEN) {
            addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), ChessPiece.PieceType.QUEEN));
        }
        else if (move.getPromotionPiece() == ChessPiece.PieceType.KNIGHT) {
            addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), ChessPiece.PieceType.KNIGHT));
        }
        else if (move.getPromotionPiece() == ChessPiece.PieceType.ROOK) {
            addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), ChessPiece.PieceType.ROOK));
        }
        else if (move.getPromotionPiece() == ChessPiece.PieceType.BISHOP) {
            addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), ChessPiece.PieceType.BISHOP));
        }
        addPiece(move.getStartPosition(), null);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(tiles, that.tiles);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(tiles);
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                if (tiles[i][j] == null) { //there might be a way to do this in ChessPiece.java
                    output.append("| |");
                }
                else {
                    output.append(String.format("|%s|",tiles[i][j]));
                }
            }
            output.append("\n");
        }
        return output.toString();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        ChessBoard clonedBoard = (ChessBoard) super.clone();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tiles[i][j] == null) {

                }
                clonedBoard.tiles[i][j] = (ChessPiece) tiles[i][j].clone();
            }
        }
        return clonedBoard;
    }
}
