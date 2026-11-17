package client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.AuthData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ServerFacade {

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private final String url;

    public ServerFacade(String url) {
        this.url = url;
    }

    private record registerRequest(String username, String password, String email) {}

    public AuthData register(String username, String password, String email) throws ServerException {
        String path = "/user";
        String method = "POST";
        return makeRequest(method, path, new registerRequest(username, password, email), AuthData.class, null);
    }

    private record loginRequest(String username, String password) {}

    public AuthData login(String username, String password) throws ServerException {
        String path = "/session";
        String method = "POST";
        return makeRequest(method, path, new loginRequest(username, password), AuthData.class, null);
    }

    public void logout(String authToken) throws ServerException {
        String path = "/session";
        String method = "DELETE";
        makeRequest(method, path, null, null, authToken);
    }

    private record createGameRequest(String gameName) {}

    private record createGameResponse(int gameID) {}

    public int createGame(String gameName, String authToken) throws ServerException {
        String path = "/game";
        String method = "POST";
        createGameResponse response = makeRequest(method,
                path, new createGameRequest(gameName), createGameResponse.class, authToken);
        return response.gameID;
    }

    public record listGameData(int gameID, String whiteUsername, String blackUsername, String gameName) {}

    private record listGamesResponse(List<listGameData> games) {}

    public List<listGameData> listGames(String authToken) throws ServerException {
        String path = "/game";
        String method = "GET";
        listGamesResponse response = makeRequest(method,
                path, null, listGamesResponse.class, authToken);
        return response.games();
    }

    private record JoinGameRequest(int gameID, String playerColor) {}

    public void joinGame(int gameID, String teamColor, String authToken) throws ServerException {
        String path = "/game";
        String method = "PUT";
        makeRequest(method, path, new JoinGameRequest(gameID, teamColor), null, authToken);
    }

    public void clear() throws ServerException {
        String path = "/db";
        String method = "DELETE";
        makeRequest(method, path, null, null, null);
    }

    //universal helper function that builds and sents a request to the server
    private <T> T makeRequest(String method,
                              String path,
                              Object body,
                              Class<T> responseClass,
                              String authToken) throws ServerException {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(url + path))
                    .timeout(java.time.Duration.ofMillis(5000));
            if (body != null) {
                builder.header("Content-Type", "application/json");
                //set method (/post, /get, etc) AND give it a body in the request
                builder.method(method, HttpRequest.BodyPublishers.ofString(gson.toJson(body)));
            } else {
                // tell the builder when we wont need a body for things like logout or list games
                builder.method(method, HttpRequest.BodyPublishers.noBody());
            }
            if (authToken != null) {
                builder.header("authorization", authToken);
            }
            HttpRequest request = builder.build();
            //send request and convert it into a response string
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                String errorMessage = getErrorMessage(response.body());
                throw new ServerException(errorMessage, response.statusCode());
            }
            //if we want a response, return the response body into a class that we need. Otherwise just return null.
            if (responseClass == null) {
                return null;
            } else {
                return gson.fromJson(response.body(), responseClass);
            }
        } catch (ServerException e) {
            throw e;
        } catch (Exception e) {
            throw new ServerException(e.getMessage(), 500);
        }
    }

    private String getErrorMessage(String body) {
        JsonObject object = JsonParser.parseString(body).getAsJsonObject();
        if (object.has("message")) {
            return object.get("message").getAsString();
        } else {
            return "Unknown error";
        }
    }

// THIS CLASS WILL MAKE REQUESTS TO THE SERVER AND TURN THEM INTO EASILY DIGESTABLE DELICIOUS JAVA OBJECTS
}
