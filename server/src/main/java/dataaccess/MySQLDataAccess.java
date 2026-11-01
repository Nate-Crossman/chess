package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.*;

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

    private static final String getUserDataStatement =
            "SELECT password, email FROM userDataSet WHERE username = ?";

    public UserData getUserData(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try(var preparedStatement = conn.prepareStatement(getUserDataStatement)) {
                preparedStatement.setString(1, username);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (!rs.next()) {
                        throw new DataAccessException("Error: bad request");
                    }
                    String password = rs.getString("password");
                    String email = rs.getString("email");
                    return new UserData(username, password, email);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private static final String addUserDataStatement =
            "INSERT INTO userDataSet (username, password, email) VALUES (?, ?, ?)";

    public AuthData addUserData(UserData inputData) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try(var preparedStatement = conn.prepareStatement(addUserDataStatement)) {
                preparedStatement.setString(1, inputData.username());
                preparedStatement.setString(2, inputData.password());
                preparedStatement.setString(3, inputData.email());
                try (ResultSet rs = preparedStatement.executeUpdate()) {
                    if (rs.next()) {
                        throw new DataAccessException("Error: already taken");
                    }
                    return createAuthData(inputData.username());
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private static final String createAuthDataStatement =
            "INSERT INTO authDataSet (token, username) VALUES (?, ?)";

    public AuthData createAuthData(String username) throws DataAccessException {
        String token = UUID.randomUUID().toString();
        try (Connection conn = DatabaseManager.getConnection()) {
            try(var preparedStatement = conn.prepareStatement(createAuthDataStatement)) {
                preparedStatement.setString(1, token);
                preparedStatement.setString(2, username);
                preparedStatement.executeUpdate();
            }
            return new AuthData(token, username);
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
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
