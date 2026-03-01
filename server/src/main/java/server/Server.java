package server;

import dataaccess.MemoryDataAccess;
import io.javalin.*;
import com.google.gson.Gson;
import io.javalin.http.Context;
import model.*;
import service.Service;

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
        UserData user = new Gson().fromJson(ctx.body(), UserData.class);
        //Handler makes sure the JSON is good
        //Give information to service to register
        AuthData data = service.register(user);
        if (data != null) {
            ctx.result(new Gson().toJson(data));
            ctx.status(200);
        } else {
            ctx.status(403);
            ctx.result("{\"message\":\"Error: username already taken\"}");
        }
        //change ctx.result to a JSON with the username and authtoken
//        Success response 	[200] { "username":"", "authToken":"" }
//        Failure response 	[400] { "message": "Error: bad request" }
//        Failure response 	[403] { "message": "Error: already taken" }
//        Failure response 	[500] { "message": "Error: (description of error)" }
    }

    private void login(Context ctx) {
        //eats a JSON with username and password
        //Handler makes sure JSON is well behaved
        //give loginRequest to register
        //return ctx.result to username and authToken or die trying
//        Success response 	[200] { "username":"", "authToken":"" }
//        Failure response 	[400] { "message": "Error: bad request" }
//        Failure response 	[401] { "message": "Error: unauthorized" }
//        Failure response 	[500] { "message": "Error: (description of error)" }
    }

    private void logout(Context ctx) {
        //eats JSON with authToken
        //Handler does it's thing
        //Call the service for a hitman
        //tell the president the mission was a success
//        [200] {}
//        Failure response 	[401] { "message": "Error: unauthorized" }
//        Failure response 	[500] { "message": "Error: (description of error)" }
    }

    private void listGames(Context ctx) {
        //eat JSON with authToes
        //handler handles all over the place
        //ask service unlimited bacon but no games or games,unlimited games but no games
        //ctx.result your list of games
//        Success response 	[200] { "games": [{"gameID": 1234, "whiteUsername":"", "blackUsername":"", "gameName:""} ]}
//            Failure response 	[401] { "message": "Error: unauthorized" }
//            Failure response 	[500] { "message": "Error: (description of error)" }
    }

    private void createGame(Context ctx) {
        //eat JSON with authtoken and name of your new minecraft world
        //It's handling time
        //ask your friend service to make the minecraft server
        //give ctx.result the gameID and coords to the nearest end portal
    }

    private void joinGame(Context ctx) {

    }

    private void clear(Context ctx) {
        service.clear();
        ctx.status(200);
        ctx.result("{}");
    }
}
