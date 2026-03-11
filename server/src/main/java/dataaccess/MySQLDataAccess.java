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

    public void clearGameData() {

    }

    private ResultSet executeStatement(String statement) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                ps.executeUpdate();
                return ps.getGeneratedKeys();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error executing Statement :" + statement);
        }
    }

    private String createAddUserStatement(UserData userData) {
        return "INSERT INTO userDataSet (username, password, email)" +
                "VALUES ('" + userData.username() +
                "', '" + userData.password() +
                "', '" + userData.email() + "')";
    }

    private String createGetUserStatement(String username) {
        return "SELECT * FROM userDataSet WHERE username = '" + username + "'";
    }

    private final String[] clearTablesStatements = {
            "TRUNCATE TABLE userDataSet",
            "TRUNCATE TABLE authDataSet",
            "TRUNCATE TABLE gameDataSet"
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
CREATE TABLE IF NOT EXISTS gameDataSet (
`id` int NOT NULL AUTO_INCREMENT,
`whiteUsername` varchar(255),
`blackUsername` varchar(255),
`gameName` varchar(255) NOT NULL,
`gameJSON` TEXT,
PRIMARY KEY (`id`)
)
"""
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection connection = DatabaseManager.getConnection()) {
            for (String statement : createTableStrings) {
                try (var preparedStatement = connection.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Database Configuration Failure :(");
        }
    }
}
