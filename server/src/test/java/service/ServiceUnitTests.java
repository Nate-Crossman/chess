package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

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

    @Test
    public void testCreateNewUser() {
        try {
            UserData inputData = new UserData("John Doe", "password123", "email@test.com");
            AuthData outputData = service.register(inputData);
            Assertions.assertNotNull(outputData);
            Assertions.assertEquals(inputData.username(), outputData.username());
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }
}
