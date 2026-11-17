package client;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:0");
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void resetServer() {
        try {
            serverFacade.clear();
        } catch (ServerException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
