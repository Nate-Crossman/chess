package dataaccess;

import model.*;

import java.sql.*;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;
import java.util.*;

public class MySQLDataAccess implements DataAccess {

    public MySQLDataAccess() throws DataAccessException {
        configureDatabase();
    }

    public UserData getUserData(String username) throws DataAccessException {
        String statement = createGetUserStatement(username);
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new DataAccessException("username does not exist");
                    }
                    return readUserData(rs);
                }
            }
        } catch (DataAccessException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public AuthData createUser(UserData userData) throws AlreadyTakenException {
        String statement = createGetUserStatement(userData.username());
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        throw new AlreadyTakenException("username already taken");
                    }
                }
            }
        } catch (AlreadyTakenException e) {
            throw new AlreadyTakenException("username already taken");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        statement = createAddUserStatement(userData);
        try {
            ResultSet rs = executeStatement(statement);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return createAuthData(userData.username());
    }

    private UserData readUserData(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var password = rs.getString("password");
        var email = rs.getString("email");
        return new UserData(username, password, email);
    }

    public AuthData createAuthData(String username) {
        AuthData auth = new AuthData(generateToken(), username);
        String statement = addAuthDataStatement(auth);
        try {
            executeStatement(statement);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return auth;
    }

    public boolean verifyAuthData(String authToken) {
        String statement = getAuthDataStatement(authToken);
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void removeAuthData(String authToken) {
        String statement = removeAuthDataStatement(authToken);
        try {
            executeStatement(statement);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
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

    public void clearAuthData() throws DataAccessException{
        executeStatement(clearTablesStatements[0]);
    }

    public void clearUserData() throws DataAccessException{
        executeStatement(clearTablesStatements[1]);
    }

    public void clearGameData() throws DataAccessException {
        executeStatement(clearTablesStatements[2]);
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

    private String addAuthDataStatement(AuthData authData) {
        return "INSERT INTO authDataSet (authToken, username)" +
                "VALUES ('" + authData.authToken() +
                "', '" + authData.username() + "')";
    }

    private String getAuthDataStatement(String authToken) {
        return "SELECT * FROM authDataSet WHERE authToken = '" + authToken + "'";
    }

    private String removeAuthDataStatement(String authToken) {
        return "DELETE FROM authDataSet WHERE authToken = '" + authToken + "'";
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

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }


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
