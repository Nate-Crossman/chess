package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.Connection;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;

public class MySQLDataAccess implements DataAccess {

    public MySQLDataAccess() throws DataAccessException {
        configureDatabase();
    }

    private final String[] createStatements = {
            """
CREATE TABLE IF NOT EXISTS userDataSet (
`username` varchar(255) NOT NULL,
`password` varchar(255) NOT NULL,
`email` varchar(255) NOT NULL,
PRIMARY KEY (`username`)
)
""\",
            ""\"
CREATE TABLE IF NOT EXISTS authDataSet (
`authToken` varchar(255) NOT NULL,
`username` varchar(255) NOT NULL,
PRIMARY KEY (`authToken`)
)
""\",
            ""\"
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
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try(var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public UserData getUserData(String username) throws DataAccessException {
        return null;
    }

    public AuthData addUserData(UserData inputData) throws DataAccessException {
        return null;
    }

    public AuthData createAuthData(String username) {
        return null;
    }

    public void removeAuthData(String authToken) throws DataAccessException {

    }

    public boolean hasAuthToken(String authToken) {
        return false;
    }

    public int createGame(String gameName) {
        return 0;
    }

    public Collection<GameData> getListGames() {
        return null;
    }

    public GameData getGame(String gameID) throws DataAccessException {
        return null;
    }

    public String getUsername(String authToken) throws DataAccessException {
        return null;
    }

    public void updateGame(String gameID, GameData updatedGame){

    }

    private final String[] clearStatements = {
            " TRUNCATE TABLE IF EXISTS userDataSet",
            " TRUNCATE TABLE IF EXISTS authDataSet",
            " TRUNCATE TABLE IF EXISTS gameDataSet",
    };

    public void clearData() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : clearStatements) {
                try(var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
