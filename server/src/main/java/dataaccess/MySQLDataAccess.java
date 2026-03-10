package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

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

    private String createAddUserStatement() {
        return """
                
                """;
    }

    private final String[] createTableStrings = {
            """
CREATE TABLE IF NOT EXISTS userDataSet (
`username` varchar(255) NOT NULL,
`password` varchar(255) NOT NULL,
`email` varchar(255) NOT NULL,
PRIMARY KEY (`username`)
)
"""
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection connection = DatabaseManager.getConnection()) {

        } catch (SQLException e) {
            throw new DataAccessException("Database Configuration Failure :(");
        }
    }
}
