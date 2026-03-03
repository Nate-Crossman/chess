package service;

import chess.ChessGame;
import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import org.junit.jupiter.api.*;
import model.*;

import java.util.Collection;
import java.util.HashSet;

public class ServiceUnitTests {
    private static Service service;

    @BeforeAll
    public static void init() {
        service = new Service(new MemoryDataAccess());
    }

    @BeforeEach
    public void setup() {
        service.clear();
    }

    /// REGISTER TESTS

    @Test
    public void testRegisterNewUser() {
        UserData input = new UserData("JohnTest","password","email@email.com");
        AuthData output = service.register(input);
        Assertions.assertNotNull(output);
        Assertions.assertEquals(input.username(),output.username());
    }

    @Test
    public void testRegisterBadInput() {
        UserData input = new UserData("ForgetfulFrank","ForgotMyEmail", null);
        Exception e = Assertions.assertThrows(BadRequestException.class, () -> {
           service.register(input);
        });
        Assertions.assertEquals("bad request", e.getMessage());
    }

    @Test
    public void testMultipleInputs() {
        UserData input = new UserData("JohnTest","password","email@email.com");
        AuthData outputJohn = service.register(input);
        UserData differentUsernameInput = new UserData("JaneTest","password","email@email.com");
        AuthData outputJane = service.register(differentUsernameInput);

        Assertions.assertNotNull(outputJohn);
        Assertions.assertNotNull(outputJane);
        Assertions.assertNotEquals(outputJohn.authToken(),outputJane.authToken());
    }

    @Test
    public void testDuplicateInputs() {
        UserData input = new UserData("JohnTest","password","email@email.com");
        service.register(input);
        UserData duplicateUsernameInput = new UserData("JohnTest","password","email@email.com");
        Exception e = Assertions.assertThrows(AlreadyTakenException.class, () -> {
            service.register(duplicateUsernameInput);
        });
        Assertions.assertEquals("username already taken", e.getMessage());
    }

    /// Login Tests

    @Test
    public void testLoginExistingUser() {
        UserData input = new UserData("JohnTest","password","email@email.com");
        service.register(input);

        LoginRequest request = new LoginRequest("JohnTest", "password");
        try {
            AuthData output = service.login(request);
            Assertions.assertNotNull(output);
            Assertions.assertEquals(input.username(),output.username());
            Assertions.assertEquals(request.username(),output.username());
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    public void testLoginNonExistingUser() {
        LoginRequest request = new LoginRequest("JohnTest", "password");
        Exception e = Assertions.assertThrows(DataAccessException.class, () -> {
            service.login(request);
        });
        Assertions.assertEquals("unauthorized", e.getMessage());
    }

    @Test
    public void testLoginWrongPassword() {
        UserData input = new UserData("JohnTest","password","email@email.com");
        service.register(input);
        LoginRequest request = new LoginRequest("JohnTest", "wrongPassword");
        Exception e = Assertions.assertThrows(DataAccessException.class, () -> {
            service.login(request);
        });
        Assertions.assertEquals("unauthorized", e.getMessage());
    }

    @Test
    public void testLoginBadInput() {
        LoginRequest request = new LoginRequest("JohnTest", null);
        Exception e = Assertions.assertThrows(BadRequestException.class, () -> {
            service.login(request);
        });
        Assertions.assertEquals("bad request", e.getMessage());
    }

    /// Logout Tests

    @Test
    public void testLogoutExistingUser() {
        UserData input = new UserData("JohnTest","password","email@email.com");
        AuthData authData = service.register(input);
        try {
            service.logout(authData.authToken());
            Exception e = Assertions.assertThrows(DataAccessException.class, () -> {
                service.createGame(authData.authToken(), new CreateGameRequest("game"));
            });
            Assertions.assertEquals("unauthorized", e.getMessage());
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    public void testLogoutNonExistingUser() {
        AuthData fakeAuthData = new AuthData("NonexistentToken", "EvilJohnTest");
        Exception e = Assertions.assertThrows(DataAccessException.class, () -> {
            service.logout(fakeAuthData.authToken());
        });
        Assertions.assertEquals("unauthorized", e.getMessage());
    }

    ///  Create Games

    @Test
    public void testCreateGame() {
        UserData input = new UserData("JohnTest","password","email@email.com");
        AuthData authData = service.register(input);
        CreateGameRequest request = new CreateGameRequest("TestGame");
        try {
            int gameID = service.createGame(authData.authToken(), request);
            if (gameID <= 0) {
                Assertions.fail();
            }
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void testCreateGameBadInput() {
        UserData input = new UserData("JohnTest","password","email@email.com");
        AuthData authData = service.register(input);
        CreateGameRequest FaultyRequest = new CreateGameRequest(null);
        Exception e = Assertions.assertThrows(BadRequestException.class, () -> {
            service.createGame(authData.authToken(), FaultyRequest);
        });
        Assertions.assertEquals("bad request", e.getMessage());
    }

    @Test
    public void testCreateGameUnauthorized() {
        CreateGameRequest request = new CreateGameRequest("unauthorizedGame");
        Exception e = Assertions.assertThrows(DataAccessException.class, () -> {
            service.createGame("fakeAuthToken", request);
        });
        Assertions.assertEquals("unauthorized", e.getMessage());
    }

    @Test
    public void testCreateDifferentGameIDs() {
        UserData input = new UserData("JohnTest","password","email@email.com");
        AuthData authData = service.register(input);
        CreateGameRequest request1 = new CreateGameRequest("TestGame");
        CreateGameRequest request2 = new CreateGameRequest("TestGame");
        try {
            int id1 = service.createGame(authData.authToken(), request1);
            int id2 = service.createGame(authData.authToken(), request2);
            Assertions.assertNotEquals(id1, id2);
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    /// List Games

    @Test
    public void testEmptyListGames() {
        UserData input = new UserData("JohnTest","password","email@email.com");
        AuthData authData = service.register(input);
        try {
            Collection<GameData> list = service.listGames(authData.authToken());
            Collection<GameData> expected = new HashSet<GameData>();
            Assertions.assertEquals(expected.toString(), list.toString());
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void testListGame() {
        service = new Service(new MemoryDataAccess()); //this is to compare ids easier
        UserData input = new UserData("JohnTest","password","email@email.com");
        AuthData authData = service.register(input);
        try {
            CreateGameRequest request = new CreateGameRequest("TestGame");
            service.createGame(authData.authToken(), request);
            Collection<GameData> list = service.listGames(authData.authToken());
            Collection<GameData> expected = new HashSet<GameData>();
            expected.add(new GameData(1,
                    null,
                    null,
                    "TestGame",
                    new ChessGame()));
            Assertions.assertEquals(expected.toString(), list.toString());
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void testListGameUnauthorized() {
        Exception e = Assertions.assertThrows(DataAccessException.class, () -> {
            service.listGames("FakeAuthToken");
        });
        Assertions.assertEquals("unauthorized", e.getMessage());
    }

    /// Join Game

    @Test
    public void testJoinGameAsWhite() {
        service = new Service(new MemoryDataAccess()); //this is to compare ids easier
        UserData input = new UserData("JohnTest","password","email@email.com");
        AuthData authData = service.register(input);
        CreateGameRequest request = new CreateGameRequest("TestGame");
        try {
            int gameID = service.createGame(authData.authToken(), request);
            JoinGameRequest joinRequest = new JoinGameRequest("WHITE", gameID);
            service.joinGame(authData.authToken(), joinRequest);
            Collection<GameData> list = service.listGames(authData.authToken());
            Collection<GameData> expected = new HashSet<GameData>();
            expected.add(new GameData(1,
                    "JohnTest",
                    null,
                    "TestGame",
                    new ChessGame()));
            Assertions.assertEquals(expected.toString(), list.toString());
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void testJoinGameAsBlack() {
        service = new Service(new MemoryDataAccess()); //this is to compare ids easier
        UserData input = new UserData("JohnTest","password","email@email.com");
        AuthData authData = service.register(input);
        CreateGameRequest request = new CreateGameRequest("TestGame");
        try {
            int gameID = service.createGame(authData.authToken(), request);
            JoinGameRequest joinRequest = new JoinGameRequest("BLACK", gameID);
            service.joinGame(authData.authToken(), joinRequest);
            Collection<GameData> list = service.listGames(authData.authToken());
            Collection<GameData> expected = new HashSet<GameData>();
            expected.add(new GameData(1,
                    null,
                    "JohnTest",
                    "TestGame",
                    new ChessGame()));
            Assertions.assertEquals(expected.toString(), list.toString());
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void testJoinGameAlreadyTaken() {
        service = new Service(new MemoryDataAccess()); //this is to compare ids easier
        UserData userJohn = new UserData("JohnTest","password","email@email.com");
        UserData userJane = new UserData("JaneTest","password","email@email.com");
        AuthData authJohn = service.register(userJohn);
        AuthData authJane = service.register(userJane);
        CreateGameRequest request = new CreateGameRequest("TestGame");
        try {
            int gameID = service.createGame(authJohn.authToken(), request);
            JoinGameRequest joinRequest = new JoinGameRequest("WHITE", gameID);
            service.joinGame(authJohn.authToken(), joinRequest);
            Exception e = Assertions.assertThrows(AlreadyTakenException.class, () -> {
                service.joinGame(authJane.authToken(), joinRequest);
            });
            Assertions.assertEquals("already taken", e.getMessage());
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void testJoinGameInvalidInput() {
        UserData input = new UserData("JohnTest","password","email@email.com");
        AuthData authData = service.register(input);
        JoinGameRequest badRequest = new JoinGameRequest("GREY", 67);
        Exception e = Assertions.assertThrows(BadRequestException.class, () -> {
            service.joinGame(authData.authToken(), badRequest);
        });
        Assertions.assertEquals("bad request", e.getMessage());
    }

    @Test
    public void testJoinGameUnauthorized() {
        JoinGameRequest request = new JoinGameRequest("WHITE", 1);
        Exception e = Assertions.assertThrows(DataAccessException.class, () -> {
            service.joinGame("fakeAuthToken", request);
        });
        Assertions.assertEquals("unauthorized", e.getMessage());
    }
}
