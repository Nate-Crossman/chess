package service;

import dataaccess.MemoryDataAccess;
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


}
