package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import org.junit.jupiter.api.*;
import model.*;

import java.util.Collection;
import java.util.HashSet;

public class DatabaseUnitTests {
    private static DataAccess dataAccess;

    @BeforeAll
    public static void init() {
        try {
            dataAccess = new MySQLDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setup() {
        try {
            dataAccess.clearUserData();
            dataAccess.clearAuthData();
            dataAccess.clearGameData();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private UserData testUserData = new UserData("JohnTest", "Password", "email@email.com");

    @Test
    public void testCreateUser() {
        try {
            AuthData output = dataAccess.createUser(testUserData);
            Assertions.assertEquals("JohnTest", output.username());
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void testCreateExistingUser() {
        dataAccess.createUser(testUserData);
        Exception e = Assertions.assertThrows(AlreadyTakenException.class, () -> {
            dataAccess.createUser(testUserData);
        });
        Assertions.assertEquals("username already taken", e.getMessage());;
    }

    @Test
    public void testGetUserData() {
        dataAccess.createUser(testUserData);
        try {
            UserData output = dataAccess.getUserData("JohnTest");
            Assertions.assertEquals("JohnTest", output.username());
            Assertions.assertEquals("email@email.com", output.email());
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    public void testGetNonExistingUser() {
        Exception e = Assertions.assertThrows(DataAccessException.class, () -> {
            dataAccess.getUserData("JohnTest");
        });
        Assertions.assertEquals("username does not exist", e.getMessage());
    }

    @Test
    public void testCreateAuthData() {
        try {
            AuthData output = dataAccess.createAuthData("JohnTest");
            AuthData output2 = dataAccess.createAuthData("JaneTest");
            Assertions.assertEquals("JohnTest", output.username());
            Assertions.assertNotEquals(output.authToken(), output2.authToken());
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void testVerifyAuthData() {
        AuthData auth = dataAccess.createUser(testUserData);
        Assertions.assertTrue(dataAccess.verifyAuthData(auth.authToken()));
    }

    @Test
    public void testVerifyFalseData() {
        Assertions.assertFalse(dataAccess.verifyAuthData("badAuthToken"));
    }

    @Test
    public void testRemoveAuthData() {
        AuthData auth = dataAccess.createUser(testUserData);
        dataAccess.removeAuthData(auth.authToken());
        Assertions.assertFalse(dataAccess.verifyAuthData(auth.authToken()));
    }

    @Test
    public void testCreateGame() {
        try {
            int id = dataAccess.createGame("TestGame");
            Assertions.assertTrue( id != 0);
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void testGetGame() {
        try {
            ChessGame expected = new ChessGame();
            int id = dataAccess.createGame("TestGame");
            GameData data = dataAccess.getGame(id);
            Assertions.assertEquals(expected, data.game());
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void testGetNonExistingGame() {
        GameData result = dataAccess.getGame(404);
        Assertions.assertNull(result);
    }

    @Test
    public void testListGamesEmpty() {
        Collection<GameData> result = dataAccess.listGames();
        Assertions.assertEquals(0, result.size());
    }

    @Test
    public void testListGames() {
        int game1 = dataAccess.createGame("game1");
        int game2 = dataAccess.createGame("game2");
        Collection<GameData> result = dataAccess.listGames();
        Assertions.assertEquals(2, result.size());
    }

    @Test
    public void testUpdateGame() {
        int gameID = dataAccess.createGame("TestGame");
        GameData gameData = dataAccess.getGame(gameID);
        GameData changedGame = gameData.addWhitePlayer("JohnTest");
        changedGame = changedGame.addBlackPlayer("JaneTest");
        dataAccess.updateGame(gameID, changedGame);
        GameData result = dataAccess.getGame(gameID);
        Assertions.assertEquals("JohnTest", result.whiteUsername());
        Assertions.assertEquals("JaneTest", result.blackUsername());
    }

    @Test
    public void testClear() {
        dataAccess.createUser(testUserData);
        int id = dataAccess.createGame("Test");
        try {
            dataAccess.clearUserData();
            dataAccess.clearAuthData();
            dataAccess.clearGameData();
            GameData result = dataAccess.getGame(id);
            Assertions.assertNull(result);
            Exception e = Assertions.assertThrows(DataAccessException.class, () -> {
                dataAccess.getUserData("JohnTest");
            });
            Assertions.assertEquals("username does not exist", e.getMessage());
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }
}

