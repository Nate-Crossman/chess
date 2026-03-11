package dataaccess;

import chess.ChessGame;
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


}
