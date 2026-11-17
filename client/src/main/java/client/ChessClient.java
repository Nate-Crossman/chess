package client;

import chess.ChessBoard;
import model.*;
import ui.DrawChessBoard;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

//wow this is a really stupid way to do this but if it aint broke dont fix it, eh?
class ClientState {
//    Default states on bootup
    boolean running = true;
    boolean logged_in = false;
    String authToken;
    Map<Integer, Integer> gameIDs = new HashMap<>();
    int idCount = 1;
}

public class ChessClient {

    private static final String HELP_LOGGED_OUT_MESSAGE = """
            Available commands:
            register <Username> <Password> <Email> -- creates a new account
            login <Username> <Password> -- login to an existing account
            quit -- exit the application
            help -- display available commands
            """;

    private static final String HELP_LOGGED_IN_MESSAGE = """
            Available commands:
            create <game name> -- create a new chess game
            list -- list all available games
            join <ID> [WHITE|BLACK] -- join the game as the provided color
            observe <ID> -- spectate a game
            logout -- sign out of current user
            quit -- signs out and exits the application
            """;

    private final String url;
    private final ServerFacade serverFacade;
    private final DrawChessBoard drawChessBoard;

    public ChessClient(String url) {
        this.url = url;
        serverFacade = new ServerFacade(url);
        drawChessBoard = new DrawChessBoard();
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
                case "help", "h" -> System.out.print(HELP_LOGGED_IN_MESSAGE);
                case "logout", "l" -> logout(state);
                case "create", "c" -> createGame(responseParts, state);
                case "list", "ls" -> listGames(state);
                case "play", "p", "join", "j" -> joinGame(responseParts, state);
                case "observe", "o" -> {
                    ChessBoard board = new ChessBoard();
                    board.resetBoard();
                    drawChessBoard.printChessBoard(board, false);
                }
                default -> System.out.println("Command not recognized. Type \"help\" for list of commands.");
            }
        } else {
            switch (responseParts[0]) {
                case "help", "h" -> System.out.print(HELP_LOGGED_OUT_MESSAGE);
                case "register", "r" -> register(responseParts, state);
                case "login", "l" -> login(responseParts, state);
                case "quit", "q" -> state.running = false;
                default -> System.out.println("Command not recognized. Type \"help\" for list of commands.");
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
            System.out.println(String.format("Register SUCCESS: Signed in as %s", data.username()));
        } catch (ServerException e) {
            String errorMessage = "Register FAILED";
            switch (e.statusCode) {
                case 400 -> errorMessage = errorMessage + ": Bad Request";
                case 403 -> errorMessage = errorMessage + ": Username already taken";
                default -> {}
            }
            System.out.println(errorMessage);
        }
    }

    private void login(String[] responseParts, ClientState state) {
        if (responseParts.length != 3) {
            System.out.println("Login requires 2 arguments: <username>, <password>");
            return;
        }
        try {
            AuthData data = serverFacade.login(responseParts[1], responseParts[2]);
            state.authToken = data.authToken();
            state.logged_in = true;
            System.out.println(String.format("Login SUCCESS: Signed in as %s", data.username()));
        } catch (ServerException e) {
            String errorMessage = "Login FAILED";
            switch (e.statusCode) {
                case 400 -> errorMessage = errorMessage + ": Bad Request";
                case 401 -> errorMessage = errorMessage + ": Wrong password!";
                default -> {}
            }
            System.out.println(errorMessage);
        }
    }

    private void logout(ClientState state) {
        try {
            serverFacade.logout(state.authToken);
            state.authToken = null;
            state.logged_in = false;
            System.out.println("Logout SUCCESS: Bye bye!");
        } catch (ServerException e) {
            String errorMessage = "Logout FAILED";
            switch (e.statusCode) {
                case 401 -> errorMessage = errorMessage + ": Authentication not recognized. Try restarting the application";
                default -> {}
            }
            System.out.println(errorMessage);
        }
    }

    private void createGame(String[] responseParts, ClientState state) {
        if (responseParts.length != 2) {
            System.out.println("Create requires 1 argument: <game name>");
            return;
        }
        try {
            int gameID = serverFacade.createGame(responseParts[1], state.authToken);
            state.gameIDs.put(state.idCount, gameID);
            state.idCount++;
            System.out.println("Create SUCCESS: You can view the created game by running the list command!");
        } catch (ServerException e) {
            String errorMessage = "Create FAILED";
            switch (e.statusCode) {
                case 400 -> errorMessage = errorMessage + ": Bad Request";
                case 401 -> errorMessage = errorMessage + ": Authentication not recognized. Try restarting the application";
                default -> {}
            }
            System.out.println(errorMessage);
        }
    }

    private void listGames(ClientState state) {
        try {
            List<ServerFacade.ListGameData> gameList = serverFacade.listGames(state.authToken);
            if (gameList.isEmpty()) {
                System.out.println("List SUCCESS: But there's no games! Create one by using the \"create\" command!");
            } else {
                state.gameIDs.clear();
                for (int i = 0; i < gameList.size(); i++) {
                    ServerFacade.ListGameData game = gameList.get(i);
                    state.gameIDs.put(i+1, game.gameID());
                    String wu;
                    String bu;
                    if (game.whiteUsername() == null) {
                        wu = "[ OPEN ]";
                    } else {
                        wu = game.whiteUsername();
                    }
                    if (game.blackUsername() == null) {
                        bu = "[ OPEN ]";
                    } else {
                        bu = game.blackUsername();
                    }
                    String out = String.format("%d) %s | White: %s | Black: %s",i+1, game.gameName(), wu, bu);
                    System.out.println(out);
                }
                System.out.println("List SUCCESS: Join a listed game by running \"join\" with the ID and Color");
            }
        } catch (ServerException e) {
            String errorMessage = "List FAILED";
            switch (e.statusCode) {
                case 401 -> errorMessage = errorMessage + ": Authentication not recognized. Try restarting the application";
                default -> {}
            }
            System.out.println(errorMessage);
        }
    }

    private void joinGame(String[] responseParts, ClientState state) {
        if (responseParts.length != 3) {
            System.out.println("Play requires 2 arguments: <gameID>, [BLACK|WHITE]");
            return;
        }
        if (state.gameIDs.isEmpty()) {
            System.out.println("Uh oh! No valid games are found!" +
                    "\n Try running \"list\" to get games from the server.");
            return;
        }
        String color = responseParts[2];
        if (!(color.equals("WHITE") || color.equals("BLACK"))) {
            System.out.println("Team color can only be \"WHITE\" or \"BLACK\". This is case sensitive.");
            return;
        }
        try {
            int gameID = Integer.parseInt(responseParts[1]);
            if (!state.gameIDs.containsKey(gameID)) {
                System.out.println(String.format("%d is not in the list of games. Run \"list\" to see available games",
                        gameID));
                return;
            }
            int serverID = state.gameIDs.get(gameID);
            serverFacade.joinGame(serverID, color, state.authToken);
            System.out.println(String.format("Play SUCCESS: Joined game as %s", color));
        } catch (NumberFormatException e) {
            System.out.println("Play FAILED: Make sure <gameID> is a number!");
            return;
        } catch (ServerException e) {
            String errorMessage = "Play FAILED";
            switch (e.statusCode) {
                case 400 -> errorMessage += ": Bad Request";
                case 401 -> errorMessage += ": Authentication not recognized. Try restarting the application";
                case 403 -> errorMessage += String.format(": %s is already taken by another player!", color);
                default -> {}
            }
            System.out.println(errorMessage);
        }


    }


//    THIS CLASS DEALS WITH UI AND THE USER INPUT, GIVES IT TO SERVERFACADE TO ACTUALLY INTERACT W/ THE SERVER
//    HANDLES ERRORS HERE IN A PRETTY WAY
}
