package model;

public record LoginRequest(String username, String password) {
    public boolean isValidLoginRequest() {
        return (username != null && password != null);
    }
}
