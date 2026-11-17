package client;

import dataaccess.DataAccessException;
import model.AuthData;
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
        serverFacade = new ServerFacade("http://localhost:" + port);
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
    public void testRegister() {
        AuthData data1 = serverFacade.register("John Test", "password", "email@email.com");
        AuthData data2 = serverFacade.register("Jane Test", "password", "email@email.com");
        Assertions.assertNotEquals(data1.authToken(), data2.authToken());
        //duplicate user throws error
        Assertions.assertThrows(ServerException.class, () ->
                serverFacade.register("John Test", "password", "email@email.com"));
    }

    @Test
    public void testLogin() {
        AuthData data1 = serverFacade.register("John Test", "password", "email@email.com");
        AuthData data2 = serverFacade.login("John Test", "password");
        Assertions.assertNotEquals(data1.authToken(), data2.authToken());
        Assertions.assertEquals(data1.username(), data2.username());
        Assertions.assertThrows(ServerException.class, () ->
                serverFacade.login("John Test", "wrong_password"));
    }

    @Test
    public void testLogout() {
        AuthData data1 = serverFacade.register("John Test", "password", "email@email.com");
        serverFacade.createGame("test", data1.authToken());
        serverFacade.logout(data1.authToken());
        Assertions.assertThrows(ServerException.class, () ->
                serverFacade.createGame("test", data1.authToken()));
    }



}
