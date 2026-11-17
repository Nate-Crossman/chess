package ui;


import chess.ChessGame;
import chess.ChessPiece;

class UITile {
    private final String character;
    private final String bgColor;
    private final String textColor;

    //cool awesome piece constructor
    UITile(ChessPiece piece, String bgColor) {
        this.bgColor = bgColor;
        if (piece == null) {
            character = EscapeSequences.EMPTY;
            textColor = EscapeSequences.SET_TEXT_COLOR_BLACK;
            return;
        }
        switch (piece.getPieceType()) {
            case KING -> character = EscapeSequences.BLACK_KING;
            case QUEEN -> character = EscapeSequences.BLACK_QUEEN;
            case BISHOP -> character = EscapeSequences.BLACK_BISHOP;
            case KNIGHT -> character = EscapeSequences.BLACK_KNIGHT;
            case ROOK -> character = EscapeSequences.BLACK_ROOK;
            case PAWN -> character = EscapeSequences.BLACK_PAWN;
            default -> character = EscapeSequences.EMPTY;
        }
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            textColor = EscapeSequences.SET_TEXT_COLOR_BLUE;
        } else {
            textColor = EscapeSequences.SET_TEXT_COLOR_RED;
        }
    }

    //cool awesome boarder constructor
    UITile(String character) {
        if (character == null) {
            this.character = EscapeSequences.EMPTY;
        } else {
            this.character = character;
        }
        this.textColor = EscapeSequences.SET_TEXT_COLOR_BLACK;
        this.bgColor = EscapeSequences.SET_BG_COLOR + "187m"; //beige

    }

    String displayTile() {
        return bgColor + textColor + character + EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.RESET_BG_COLOR;
    }
}

public class DrawChessBoard {

    public void printChessBoard(ChessGame game, boolean isWhite) {
        UITile[][] board = buildBoard(game);
        if (isWhite) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    System.out.print(board[i][j].displayTile());
                }
            }
        } else {
            for (int i = 9; i >= 0; i--) {
                for (int j = 9; j >=0; j--) {
                    System.out.print(board[i][j].displayTile());
                }
            }
        }
    }

    private UITile[][] buildBoard(ChessGame game) {
        UITile[][] board = new UITile[10][10];
        //get boarders
        return board;
    }

}
