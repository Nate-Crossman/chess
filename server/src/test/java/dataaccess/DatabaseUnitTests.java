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


}
