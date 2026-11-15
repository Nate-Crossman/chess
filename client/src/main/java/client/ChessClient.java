package client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;
import java.util.Scanner;

class ClientState {
//    Default states on bootup
    boolean running = true;
    boolean logged_in = false;
}

public class ChessClient {

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public void run() {
        Scanner input = new Scanner(System.in);
        ClientState state = new ClientState();
        while (state.running) {
            if (state.logged_in) {
                System.out.print("[LOGGED IN] > ");
            } else {
                System.out.print("[LOGGED OUT] > ");
            }
            String response = input.nextLine();
            handle(response, state);
            System.out.print(String.format("You just said: %s\n", response));
        }
        System.out.print("You quit the program. Yippee!");
    }

    private void handle(String response, ClientState state) {
        String[] responseParts = response.split("\\s+", 3);
        if (state.logged_in) {
            switch (response) {
                case "help", "h" -> {}
                case "logout", "l" -> {state.logged_in = false;}
                case "create", "c" -> {}
                case "list", "ls" -> {}
                case "play", "p" -> {}
                case "observe", "o" -> {}
                default -> System.out.print("Command not recognized. Type \"help\" for list of commands.");
            }
        } else {
            switch (response) {
                case "help", "h" -> {}
                case "register", "r" -> register(responseParts, state);
                case "login", "l" -> {state.logged_in = true;} //code for testing will change (:
                case "quit", "q" -> {state.running = false;}
                default -> System.out.print("Command not recognized. Type \"help\" for list of commands.");
            }
        }
    }

    private void register(String[] responseParts, ClientState state) {
        if (responseParts.length != 4) {
            System.out.print("Register requires 3 arguments: <username>, <password>, <email>");
        }

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
