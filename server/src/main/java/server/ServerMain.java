package server;

import chess.*;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import dataaccess.MySQLDataAccess;
import service.Service;

public class ServerMain {

    public static void main(String[] args) {
        try {
            var port = 8080;
            if (args.length >= 1) {
                port = Integer.parseInt(args[0]);
            }

            DataAccess dataAccess = new MemoryDataAccess();
            if (args.length >= 2 && args[1].equals("sql")) {
                dataAccess = new MySQLDataAccess();
            }

            var service = new Service(dataAccess);
            var server = new Server(service);
            server.run(8080);
            System.out.println("♕ 240 Chess Server");
        } catch (Exception e) {
            System.out.printf("Unable to start server: %s", e.getMessage());
        }
    }
}
