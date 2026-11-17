package client;

import dataaccess.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;

import java.util.List;


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

    @Test
    public void testCreateGame() {
        AuthData data1 = serverFacade.register("John Test", "password", "email@email.com");
        serverFacade.createGame("test", data1.authToken());
        List<ServerFacade.ListGameData> gameList = serverFacade.listGames(data1.authToken());
        Assertions.assertEquals(1, gameList.size());
        Assertions.assertThrows(ServerException.class, () ->
                serverFacade.createGame("test", "fake_auth_token"));
    }

    @Test
    public void testListGames() {
        AuthData data1 = serverFacade.register("John Test", "password", "email@email.com");
        serverFacade.createGame("test", data1.authToken());
        List<ServerFacade.ListGameData> gameList = serverFacade.listGames(data1.authToken());
        Assertions.assertEquals(1, gameList.size());
        serverFacade.createGame("test", data1.authToken());
        serverFacade.createGame("test", data1.authToken());
        gameList = serverFacade.listGames(data1.authToken());
        Assertions.assertEquals(3, gameList.size());
        Assertions.assertThrows(ServerException.class, () ->
                serverFacade.listGames("fake_auth_token"));
    }

    @Test
    public void testJoinGames() {
        AuthData john = serverFacade.register("John Test", "password", "john@email.com");
        AuthData jane = serverFacade.register("Jane Test", "password", "jane@email.com");
        AuthData jim = serverFacade.register("Jim Test", "password", "jim@email.com");
        int gameID = serverFacade.createGame("test", john.authToken());
        serverFacade.joinGame(gameID, "WHITE", john.authToken());
        serverFacade.joinGame(gameID, "BLACK", jane.authToken());
        List<ServerFacade.ListGameData> gameList = serverFacade.listGames(john.authToken());
        Assertions.assertEquals(1, gameList.size());
        Assertions.assertEquals("John Test", gameList.get(0).whiteUsername());
        Assertions.assertEquals("Jane Test", gameList.get(0).blackUsername());
        Assertions.assertThrows(ServerException.class, () ->
                serverFacade.joinGame(gameID, "WHITE", jim.authToken()));
        Assertions.assertThrows(ServerException.class, () ->
                serverFacade.joinGame(gameID, "BLACK", jim.authToken()));
        int openGameID = serverFacade.createGame("open game", john.authToken());
        Assertions.assertThrows(ServerException.class, () ->
                serverFacade.joinGame(openGameID, "GREEN", john.authToken()));
        Assertions.assertThrows(ServerException.class, () ->
                serverFacade.joinGame(openGameID, "WHITE", "fake_auth_token"));
        Assertions.assertThrows(ServerException.class, () ->
                serverFacade.joinGame(400, "WHITE", john.authToken()));
    }

}
