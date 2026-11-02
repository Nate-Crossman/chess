package dataaccess.sql;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MySQLDataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

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
            dataAccess.clearData();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private UserData testUser = new UserData("Test User", "Test Password", "Test Email");


    @Test
    public void testUserData() {
        try {
            dataAccess.addUserData(testUser);
            UserData result = dataAccess.getUserData("Test User");
            Assertions.assertEquals(testUser.username(), result.username());
            Assertions.assertEquals(testUser.password(), result.password());
            Assertions.assertEquals(testUser.email(), result.email());
            //test repeat user
            assertThrows(DataAccessException.class, () -> dataAccess.addUserData(testUser));
            //test nonexistant user
            assertThrows(DataAccessException.class, () -> dataAccess.getUserData("Nobody"));
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    public void testAuthData() {
        try {
            AuthData authData = dataAccess.createAuthData("Test User");
            assertTrue(dataAccess.hasAuthToken(authData.authToken()));
            String username = dataAccess.getUsername(authData.authToken());
            assertEquals("Test User", username);

            assertThrows(DataAccessException.class, () -> dataAccess.removeAuthData("fake token"));
            assertThrows(DataAccessException.class, () -> dataAccess.getUsername("fake token"));

            dataAccess.removeAuthData(authData.authToken());
            assertFalse(dataAccess.hasAuthToken(authData.authToken()));

        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    public void testGameData() {
        try {
            Collection<GameData> games = dataAccess.getListGames();
            Assertions.assertEquals(0, games.size());

            int id1 = dataAccess.createGame("game1");
            Assertions.assertEquals(1, id1);

            games = dataAccess.getListGames();
            Assertions.assertEquals(1, games.size());

            GameData gameData1 = dataAccess.getGame("1");
            Assertions.assertEquals("game1", gameData1.gameName());

            GameData updatedGame = new GameData(
                    gameData1.gameID(),
                    "Test User",
                    gameData1.blackUsername(),
                    gameData1.gameName(),
                    gameData1.game());
            dataAccess.updateGame("1", updatedGame);
            GameData gameData2 = dataAccess.getGame("1");
            Assertions.assertNotEquals(gameData1.whiteUsername(), gameData2.whiteUsername());

            dataAccess.createGame("game2");
            games = dataAccess.getListGames();
            Assertions.assertEquals(2, games.size());

            assertThrows(DataAccessException.class, () -> dataAccess.getGame("7"));

        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }
}
