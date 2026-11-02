package dataaccess.sql;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MySQLDataAccess;
import org.junit.jupiter.api.*;

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
}
