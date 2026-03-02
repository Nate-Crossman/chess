package model;

public record CreateGameRequest(String gameName) {
    public boolean isValidGameRequest() {
        return (gameName != null);
    }
}
