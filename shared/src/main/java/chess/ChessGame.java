package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    boolean isWhiteTurn;
    ChessBoard board;

    public ChessGame() {
        isWhiteTurn = true;
        board = new ChessBoard();
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        if (isWhiteTurn) return TeamColor.WHITE;
        return TeamColor.BLACK;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        if (team == TeamColor.WHITE) {
            isWhiteTurn = true;
        }
        isWhiteTurn = false;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    //returns the opposite team color
    public TeamColor oppositeTeam(TeamColor color) {
        if (color == TeamColor.WHITE) {
            return TeamColor.BLACK;
        }
        return TeamColor.WHITE;
    }

    public void changeTeamTurn() {
        if (isWhiteTurn) {
            isWhiteTurn = false;
        } else {
            isWhiteTurn = true;
        }
    }

    //returns a collection of every possible move (NOT ACCOUNTING FOR CHECK AND SUCH) by a team
    public Collection<ChessMove> allMovesByColor(ChessBoard board, TeamColor color) {
        Collection<ChessMove> output = new HashSet<>();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition position = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getTeamColor() == color) {
                    Collection<ChessMove> pieceMoves = piece.pieceMoves(board, position);
                    output.addAll(pieceMoves);
                }
            }
        }
        return output;
    }

    //returns the position of a team color's King piece
    public ChessPosition getKingPositionByColor(ChessBoard board, TeamColor color) {
        ChessPosition kingPosition = null;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition position = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(position);
                //Note -- Could Probably Improve Readability of this if statement
                if (piece != null &&
                    piece.getPieceType() == ChessPiece.PieceType.KING &&
                    piece.getTeamColor() == color) {
                    kingPosition = position;
                }
            }
        }
        return kingPosition;
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (board.getPiece(startPosition) == null) return null;
        Collection<ChessMove> allMoves = board.getPiece(startPosition).pieceMoves(board, startPosition);
        Collection<ChessMove> safeMoves = new HashSet<>();
        TeamColor pieceColor = board.getPiece(startPosition).getTeamColor();
        //iterate through every move
        for (ChessMove move : allMoves) {
            ChessBoard clonedBoard = new ChessBoard(board);
            clonedBoard.movePiece(move);
            if (!isInCheckHelper(pieceColor, clonedBoard)) {
                safeMoves.add(move);
            }

        }
        //clone board and make move
        //if move puts king in danger remove, else keep in collection
        //return collection
        return safeMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        Collection<ChessMove> validMoves = validMoves(startPosition);
        if (validMoves.contains(move)) {
            board.movePiece(move);
        } else {
            throw new InvalidMoveException();
        }
        changeTeamTurn();
    }

    //Helper function to determine if a team is in check, can be used with other boards
    public boolean isInCheckHelper(TeamColor teamColor, ChessBoard board) {
        ChessPosition kingPosition = getKingPositionByColor(board, teamColor);
        Collection<ChessMove> moves = allMovesByColor(board, oppositeTeam(teamColor));
        for (ChessMove move : moves) {
            if (move.getEndPosition().equals(kingPosition)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheckHelper(teamColor, this.board);
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
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return isWhiteTurn == chessGame.isWhiteTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isWhiteTurn, board);
    }

    @Override
    public String toString() {
        StringBuilder outputString = new StringBuilder();
        outputString.append(String.format("CHESS GAME:\n%s",board));
        //add code here later to show turn and if the game in check, checkmate, or stalemate.
        return outputString.toString();
    }
}
