package server;

import io.javalin.*;
import com.google.gson.Gson;
import io.javalin.http.Context;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

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
        //Handler makes sure the JSON is good
        //Give information to service to register
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
        //rip and tear until it is done
    }
}
