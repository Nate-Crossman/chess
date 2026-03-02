package model;

public record JoinGameRequest(String playerColor, int gameID) {
    public boolean isValidJoinGameRequest() {
        return ((playerColor != null) &&
                (playerColor.equals("WHITE") | playerColor.equals("BLACK")) &&
                gameID > 0);
    }
}