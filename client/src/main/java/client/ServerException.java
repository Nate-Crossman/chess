package client;

public class ServerException extends RuntimeException {
    public final int statusCode;

    public ServerException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
