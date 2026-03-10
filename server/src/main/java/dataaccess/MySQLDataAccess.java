package dataaccess;

import model.*;

import java.sql.*;
import java.util.Collection;

public class MySQLDataAccess implements DataAccess {

    public MySQLDataAccess() throws DataAccessException {
        configureDatabase();
    }

    public UserData getUserData(String username) throws DataAccessException {
        return null;
    }

    public AuthData createUser(UserData userData) throws AlreadyTakenException {
        return null;
    }

    public AuthData createAuthData(String username) {
        return null;
    }

    public boolean verifyAuthData(String authToken) {
        return false;
    }

    public void removeAuthData(String authToken) {

    }

    public Collection<GameData> listGames() {
        return null;
    }

    public int createGame(String gameName) {
        return 0;
    }

    public GameData getGame(int gameID) {
        return null;
    }

    public void updateGame(int gameID, GameData gameData) {

    }

    public String getUsername(String authToken) {
        return null;
    }

    public void clearAuthData() {}

    public void clearUserData() {}

    public void clearGameData() {}

    private String createAddUserStatement(UserData userData) {
        return """
                INSERT INTO userDataSet (username, password, email)
                VALUES ('JohnTest', 'p455word', 'email@example.com')
                """;
    }

    private String createGetUserStatement() {
        return """
                SELECT * FROM userDataSet
                WHERE username = 'JohnTest'
                """;
    }

    private final String[] clearTablesStatements = {
            "TRUNCATE TABLE userDataSet"
            //add clear table data as needed
    };

    private final String[] createTableStrings = {
            """
CREATE TABLE IF NOT EXISTS userDataSet (
`username` varchar(255) NOT NULL,
`password` varchar(255) NOT NULL,
`email` varchar(255) NOT NULL,
PRIMARY KEY (`username`)
)
""",
            """
CREATE TABLE IF NOT EXISTS authDataSet (
`authToken` varchar(255) NOT NULL,
`username` varchar(255) NOT NULL,
PRIMARY KEY (`authToken`)
)
""",
            """

"""
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection connection = DatabaseManager.getConnection()) {
            for (String statement : createTableStrings) {
                try (var preparedStatment = connection.prepareStatement(statement)) {
                    preparedStatment.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Database Configuration Failure :(");
        }
    }
}
