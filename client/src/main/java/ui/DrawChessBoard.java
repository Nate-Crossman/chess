package ui;


import chess.ChessGame;

record UITile(String character, String bgColor, String textColor) {
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

}
