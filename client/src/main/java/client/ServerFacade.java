package client;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private final String url;
    private String authToken;

    public ServerFacade(String url) {
        this.url = url;
    }

    public void register(String username, String password, String email) {

    }

    //universal helper function that builds and sents a request to the server
    private <T> T makeRequest(String method, String path, Object body, Class<T> responseClass) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(url + path))
                    .timeout(java.time.Duration.ofMillis(5000));
            builder.header("Content-Type", "application/json")
                    //set method (/post, /get, etc) AND give it a body in the request
                    .method(method, HttpRequest.BodyPublishers.ofString(gson.toJson(body)));
            HttpRequest request = builder.build();
            //send request and convert it into a response string
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {

            }
            //if we want a response, return the response body into a class that we need. Otherwise just return null.
            if (responseClass == null) {
                return null;
            } else {
                return gson.fromJson(response.body(), responseClass);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

// THIS CLASS WILL MAKE REQUESTS TO THE SERVER AND TURN THEM INTO EASILY DIGESTABLE DELICIOUS JAVA OBJECTS
}
