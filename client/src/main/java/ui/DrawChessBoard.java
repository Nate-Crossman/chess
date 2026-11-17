package ui;



import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

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

    public void printChessBoard(ChessBoard chessBoard, boolean isBlack) {
        UITile[][] board = buildBoard(chessBoard);
        if (isBlack) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    System.out.print(board[i][j].displayTile());
                }
                System.out.println();
            }
        } else {
            for (int i = 9; i >= 0; i--) {
                for (int j = 9; j >=0; j--) {
                    System.out.print(board[i][j].displayTile());
                }
                System.out.println();
            }
        }
    }

    private UITile[][] buildBoard(ChessBoard chessBoard) {
        UITile[][] board = new UITile[10][10];
        //CORNERS
        board[0][0] = new UITile(null);
        board[9][0] = new UITile(null);
        board[0][9] = new UITile(null);
        board[9][9] = new UITile(null);
        //lettered columns
        for (int j = 1; j < 9; j++) {
            String character = " \u2009" + (char) ('h'- j+1) + "\u2009 ";
            board[0][j] = new UITile(character);
            board[9][j] = new UITile(character);
        }
        //numbered rows
        for (int i = 1; i < 9; i++) {
            String character = " \u2009" + i + "\u2009 ";
            board[i][0] = new UITile(character);
            board[i][9] = new UITile(character);
        }
        // the board
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                String bgColor = getBgColor(i,j);
                ChessPosition pos = new ChessPosition(i,j);
                board[i][j] = new UITile(chessBoard.getPiece(pos), bgColor);
            }
        }
        return board;
    }

    String getBgColor(int i, int j) {
        if (i % 2 == 0) { //even rows
            if (j % 2 == 0) { //even col
                return EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
            } else {
                return EscapeSequences.SET_BG_COLOR_DARK_GREY;
            }
        } else {
            if (j % 2 == 0) { //even col
                return EscapeSequences.SET_BG_COLOR_DARK_GREY;
            } else {
                return EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
            }
        }
    }
}
