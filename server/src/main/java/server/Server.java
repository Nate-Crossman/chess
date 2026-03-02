package server;

import dataaccess.*;
import io.javalin.*;
import com.google.gson.Gson;
import io.javalin.http.Context;
import model.*;
import service.Service;

import java.util.Collection;

public class Server {

    private final Javalin javalin;
    private final Service service;

    public Server() {

        this.service = new Service(new MemoryDataAccess());

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", this::registration)
                .post("/session", this::login)
                .delete("/session", this::logout)
                .get("/game", this::listGames)
                .post("/game", this::createGame)
                .put("/game", this::joinGame)
                .delete("/db", this::clear);

        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void registration(Context ctx) {
        //eats a JSON that has a username, password, and email
        try {
            UserData user = new Gson().fromJson(ctx.body(), UserData.class);
            if (!user.isValidUserData()) {
                handleBadRequest(ctx);
                return;
            }
            AuthData data = service.register(user);
            if (data != null) {
                ctx.result(new Gson().toJson(data));
                ctx.status(200);
            }
        } catch (AlreadyTakenException e) {
            handleException(ctx, e, 403);
        } catch (Exception e) {
            handleException(ctx, e, 500);
        }
        //change ctx.result to a JSON with the username and authtoken
//        Success response 	[200] { "username":"", "authToken":"" }
//        Failure response 	[400] { "message": "Error: bad request" }
//        Failure response 	[403] { "message": "Error: already taken" }
//        Failure response 	[500] { "message": "Error: (description of error)" }
    }

    private void login(Context ctx) {
        try {
            LoginRequest loginRequest = new Gson().fromJson(ctx.body(), LoginRequest.class);
            //eats a JSON with username and password
            if (!loginRequest.isValidLoginRequest()) {
                handleBadRequest(ctx);
                return;
            }

                AuthData data = service.login(loginRequest);
                ctx.result(new Gson().toJson(data));
                ctx.status(200);
        } catch (BadRequestException e) {
            handleException(ctx, e, 400);
        } catch (DataAccessException e) {
            handleException(ctx, e, 401);
        } catch (Exception e) {
            handleException(ctx, e, 500);
        }
//        Success response 	[200] { "username":"", "authToken":"" }
//        Failure response 	[400] { "message": "Error: bad request" }
//        Failure response 	[401] { "message": "Error: unauthorized" }
//        Failure response 	[500] { "message": "Error: (description of error)" }
    }

    private void logout(Context ctx) {
        try {
            String authToken = ctx.header("authorization");
            service.logout(authToken);
            ctx.status(200);
        } catch (DataAccessException e) {
            handleException(ctx, e, 401);
        } catch (Exception e) {
            handleException(ctx, e, 500);
        }
    }

    private void listGames(Context ctx) {
        try {
            String authToken = ctx.header("authorization");
            Collection<GameData> gameList = service.listGames(authToken);
            ctx.result(new Gson().toJson(gameList));
            ctx.status(200);
        } catch (DataAccessException e) {
            handleException(ctx, e, 401);
        } catch (Exception e) {
            handleException(ctx, e, 500);
        }
    }
//        Success response 	[200] { "games": [{"gameID": 1234, "whiteUsername":"", "blackUsername":"", "gameName:""} ]}
//            Failure response 	[401] { "message": "Error: unauthorized" }
//            Failure response 	[500] { "message": "Error: (description of error)" }


    private void createGame(Context ctx) {
        try {
            String authToken = ctx.header("authorization");
            CreateGameRequest gameRequest = new Gson().fromJson(ctx.body(), CreateGameRequest.class);
            int gameID = service.createGame(authToken, gameRequest);
            ctx.result("{\"gameID\": \"" + gameID + "\"}");
            ctx.status(200);
        } catch (BadRequestException e) {
            handleException(ctx, e, 400);
        } catch (DataAccessException e) {
            handleException(ctx, e, 401);
        } catch (Exception e) {
            handleException(ctx, e, 500);
        }
    }

    private void joinGame(Context ctx) {

    }

    private void clear(Context ctx) {
        service.clear();
        ctx.status(200);
        ctx.result("{}");
    }

    private void handleBadRequest(Context ctx) {
        ctx.status(400);
        ctx.result("{\"message\":\"Error: bad request\"}");
    }

    private void handleException(Context ctx, Exception e, int statusNumber) {
        ctx.status(statusNumber);
        ctx.result("{\"message\":\"Error: " + e.getMessage() + "\"}");
    }
}
