package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import io.javalin.*;
import io.javalin.http.Context;
import model.AuthData;
import model.UserData;
import service.Service;

public class Server {

    private final Javalin javalin;
    private final Service service;

    public Server() {
        this.service = new Service(new MemoryDataAccess());
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", this::register)
                .delete("/db", this::clear);


        // Register your endpoints and exception handlers here.

    }
    public void register(Context ctx) throws DataAccessException {
        UserData inputData = new Gson().fromJson(ctx.body(), UserData.class);
        AuthData authData = service.register(inputData);
        ctx.status(200);
        ctx.result(new Gson().toJson(authData));

    }

    public void clear(Context ctx) {
        service.clear();
        ctx.status(200);
        ctx.result("{}");
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}


