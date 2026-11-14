package client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;
import java.util.Scanner;

public class ChessClient {

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public void run() {
        Scanner input = new Scanner(System.in);
        boolean running = true;
        boolean logged_in = false;
        while (running) {
            System.out.print("I AM RUNNING YIPPEE >");
            String response = input.nextLine();
            if (response.equals("quit") || response.equals("q")) {
                running = false;
            }
            System.out.print(String.format("You just said: %s", response));
        }
        System.out.print("You quit the program. Yippee!");
    }

    private void get(String host, int port, String path) throws Exception {
        String urlString = String.format(Locale.getDefault(), "http://%s:%d%s", host, port, path);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .GET()
                .build();

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {
            System.out.println(httpResponse.body());
        } else {
            System.out.println("Error: received status code " + httpResponse.statusCode());
        }
    }

//    THIS CLASS DEALS WITH UI AND THE USER INPUT, GIVES IT TO SERVERFACADE TO ACTUALLY INTERACT W/ THE SERVER
//    HANDLES ERRORS HERE IN A PRETTY WAY
}
