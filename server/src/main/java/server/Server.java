package server;

import dataaccess.MemoryDataAccess;
import io.javalin.*;
import io.javalin.http.Context;
import service.Service;

public class Server {

    private final Javalin javalin;
    private final Service service;

    public Server() {
        this.service = new Service(new MemoryDataAccess());
        javalin = Javalin.create(config -> config.staticFiles.add("web"))

                .delete("/db", this::clear);


        // Register your endpoints and exception handlers here.

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


