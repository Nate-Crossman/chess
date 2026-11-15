package client;

import model.*;

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
    String authToken;
}

public class ChessClient {

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private final String url;
    private final ServerFacade serverFacade;

    public ChessClient(String url) {
        this.url = url;
        serverFacade = new ServerFacade(url);
    }

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
        }
        System.out.print("You quit the program. Yippee!");
    }

    private void handle(String response, ClientState state) {
        String[] responseParts = response.trim().split("\\s+");
        if (state.logged_in) {
            switch (responseParts[0]) {
                case "help", "h" -> {}
                case "logout", "l" -> {state.logged_in = false;}
                case "create", "c" -> {}
                case "list", "ls" -> {}
                case "play", "p" -> {}
                case "observe", "o" -> {}
                default -> System.out.print("Command not recognized. Type \"help\" for list of commands.");
            }
        } else {
            switch (responseParts[0]) {
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
            return;
        }
        try {
            AuthData data = serverFacade.register(responseParts[1], responseParts[2], responseParts[3]);
            state.authToken = data.authToken();
            state.logged_in = true;
            System.out.print(String.format("Register SUCCESS: Signed in as %s", data.username()));
        } catch (ServerException e) {
            String errorMessage = "Register FAILED";
            switch (e.statusCode) {
                case 400 -> errorMessage = errorMessage + ": Bad Request";
                case 403 -> errorMessage = errorMessage + ": Username already taken";
                default -> {}
            }
            System.out.print(errorMessage);
        }
    }


//    THIS CLASS DEALS WITH UI AND THE USER INPUT, GIVES IT TO SERVERFACADE TO ACTUALLY INTERACT W/ THE SERVER
//    HANDLES ERRORS HERE IN A PRETTY WAY
}
