package dataaccess.sql;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MySQLDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

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
}
