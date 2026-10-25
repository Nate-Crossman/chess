package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServiceUnitTests {
    private static Service service;

    @BeforeAll
    public static void init(){
        service = new Service(new MemoryDataAccess());
    }

    @BeforeEach
    public void clearService() {
        service.clear();
    }

    private final UserData testUser = new UserData("John Doe", "password123", "email@test.com");

    @Test
    public void testCreateNewUser() {
        try {
            AuthData outputData = service.register(testUser);
            Assertions.assertNotNull(outputData);
            Assertions.assertEquals(testUser.username(), outputData.username());
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    public void testBadRegisterRequests() {
        UserData noUsername = new UserData(null, "password", "email@test.com");
        UserData noPassword = new UserData("John Doe", null, "email@test.com");
        UserData noEmail    = new UserData("John Doe", "password", null);
        assertThrows(DataAccessException.class, () -> service.register(noUsername));
        assertThrows(DataAccessException.class, () -> service.register(noPassword));
        assertThrows(DataAccessException.class, () -> service.register(noEmail));
    }

    @Test
    public void testAlreadyTakenRegister() {
        try {
            service.register(testUser);
            assertThrows(DataAccessException.class, () -> service.register(testUser));
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    public void testLogin() {
        try {
            AuthData data1 = service.register(testUser);
            AuthData data2 = service.login("John Doe", "password123");
            Assertions.assertNotNull(data2);
            Assertions.assertNotEquals(data1, data2);
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    public void testLoginWrongPassword() {
        try {
            service.register(testUser);
            assertThrows(DataAccessException.class, () -> service.login("John Doe", "wrong password"));
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    public void testLoginBadRequest() {
        try {
            service.register(testUser);
            assertThrows(DataAccessException.class, () -> service.login("John Doe", null));
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    public void testLogout() {
        try {
            AuthData authData = service.register(testUser);
            service.logout(authData.authToken());
            assertThrows(DataAccessException.class, () -> service.createGame(authData.authToken(), "unauthorized game"));
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    public void testLogoutIncorrectToken() {
        try {
            service.register(testUser);
            assertThrows(DataAccessException.class, () -> service.logout("wrong token"));
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

}
