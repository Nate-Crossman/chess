package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import kotlin.TuplesKt;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.*;

import java.util.Collection;
import java.util.HashSet;
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
                        throw new DataAccessException("Error: unauthorized");
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
            try(var preparedStatement = conn.prepareStatement(getUserDataStatement)) {
                preparedStatement.setString(1, inputData.username());
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        throw new DataAccessException("Error: already taken");
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        try (Connection conn = DatabaseManager.getConnection()) {
            try(var preparedStatement = conn.prepareStatement(addUserDataStatement)) {
                preparedStatement.setString(1, inputData.username());
                preparedStatement.setString(2, inputData.password());
                preparedStatement.setString(3, inputData.email());
                preparedStatement.execute();
                return createAuthData(inputData.username());
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private static final String createAuthDataStatement =
            "INSERT INTO authDataSet (authToken, username) VALUES (?, ?)";

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

    private static final String removeAuthDataStatement =
            "DELETE FROM authDataSet WHERE authToken = ?";

    public void removeAuthData(String authToken) throws DataAccessException {
        if (!hasAuthToken(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        }
        try (Connection conn = DatabaseManager.getConnection()) {
            try(var preparedStatement = conn.prepareStatement(removeAuthDataStatement)) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private static final String getAuthDataStatement =
            "SELECT * FROM authDataSet WHERE authToken = ?";

    public boolean hasAuthToken(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try(var preparedStatement = conn.prepareStatement(getAuthDataStatement)) {
                preparedStatement.setString(1, authToken);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private static final String createGameStatement =
            "INSERT INTO gameDataSet (gameName, gameJSON) VALUES (?, ?)";

    public int createGame(String gameName) throws DataAccessException {
        ChessGame game = new ChessGame();
        Gson gson = new Gson();
        String gameJSON = gson.toJson(game);
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(createGameStatement, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, gameName);
                preparedStatement.setString(2, gameJSON);
                preparedStatement.executeUpdate();

                try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                    throw new DataAccessException("Error: Unable to retrieve created game index");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private final static String getListGamesStatement =
            "SELECT * FROM gameDataSet";

    public Collection<GameData> getListGames() throws DataAccessException {
        Collection<GameData> output = new HashSet<GameData>();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(getListGamesStatement)) {
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        String gameJSON = rs.getString("gameJSON");
                        ChessGame game = new Gson().fromJson(gameJSON, ChessGame.class);
                        GameData data = new GameData(id, whiteUsername, blackUsername, gameName, game);
                        output.add(data);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return output;
    }

    private final static String getGameStatement =
            "SELECT * FROM gameDataSet WHERE id = ?";

    public GameData getGame(String gameID) throws DataAccessException {
        int intGameID = Integer.parseInt(gameID);
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(getGameStatement)) {
                preparedStatement.setInt(1, intGameID);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (!rs.next()) {
                        throw new DataAccessException("Error: bad request");
                    }
                    int id = rs.getInt("id");
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");
                    ChessGame game = new Gson().fromJson(rs.getString("gameJSON"), ChessGame.class);
                    return new GameData(id, whiteUsername, blackUsername, gameName, game);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private final static String getUsernameStatment =
            "SELECT username FROM authDataSet WHERE authtoken = ?";

    public String getUsername(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(getUsernameStatment)) {
                preparedStatement.setString(1, authToken);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (!rs.next()) {
                        throw new DataAccessException("Error: unauthorized");
                    }
                    return rs.getString("username");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private final static String updateGameStatement =
            "UPDATE gameDataSet SET whiteUsername = ?, blackUsername = ?, gameName = ?, gameJSON = ? WHERE id = ?";

    public void updateGame(String gameID, GameData updatedGame) throws DataAccessException {
        int intGameID = Integer.parseInt(gameID); //what moron turned this into a string?
        String updatedJSON = new Gson().toJson(updatedGame.game());
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(updateGameStatement)) {
                preparedStatement.setString(1,updatedGame.whiteUsername());
                preparedStatement.setString(2,updatedGame.blackUsername());
                preparedStatement.setString(3,updatedGame.gameName());
                preparedStatement.setString(4,updatedJSON);
                preparedStatement.setInt(5, intGameID);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

    }

    private final String[] clearStatements = {
            " TRUNCATE TABLE userDataSet",
            " TRUNCATE TABLE authDataSet",
            " TRUNCATE TABLE gameDataSet",
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
