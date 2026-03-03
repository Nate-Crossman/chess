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

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void registration(Context ctx) {
        try {
            UserData user = new Gson().fromJson(ctx.body(), UserData.class);
            AuthData data = service.register(user);
            if (data != null) {
                ctx.result(new Gson().toJson(data));
                ctx.status(200);
            }
        } catch (BadRequestException e) {
            handleException(ctx, e, 400);
        } catch (AlreadyTakenException e) {
            handleException(ctx, e, 403);
        } catch (Exception e) {
            handleException(ctx, e, 500);
        }
    }

    private void login(Context ctx) {
        try {
            LoginRequest loginRequest = new Gson().fromJson(ctx.body(), LoginRequest.class);
            //eats a JSON with username and password
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
            ctx.result("{\"games\": " + new Gson().toJson(gameList) + "}");
            ctx.status(200);
        } catch (DataAccessException e) {
            handleException(ctx, e, 401);
        } catch (Exception e) {
            handleException(ctx, e, 500);
        }
    }

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
        try {
            String authToken = ctx.header("authorization");
            JoinGameRequest joinRequest = new Gson().fromJson(ctx.body(), JoinGameRequest.class);
            service.joinGame(authToken, joinRequest);
            ctx.status(200);
        } catch (BadRequestException e) {
            handleException(ctx, e, 400);
        } catch (DataAccessException e) {
            handleException(ctx, e, 401);
        } catch (AlreadyTakenException e) {
            handleException(ctx, e, 403);
        } catch (Exception e) {
            handleException(ctx, e, 500);
        }
    }

    private void clear(Context ctx) {
        service.clear();
        ctx.status(200);
        ctx.result("{}");
    }

    private void handleException(Context ctx, Exception e, int statusNumber) {
        ctx.status(statusNumber);
        ctx.result("{\"message\":\"Error: " + e.getMessage() + "\"}");
    }
}
