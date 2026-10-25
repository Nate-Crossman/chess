package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;

public class Service {

    private final DataAccess dataAccess;

    public Service(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void clear() {
        dataAccess.clearData();
    }

    public AuthData register(UserData inputData) throws DataAccessException {
        if (inputData.username() == null ||
                inputData.password() == null ||
                inputData.email() == null) {
            throw new DataAccessException("Error: bad request");
        }
        return dataAccess.addUserData(inputData);
    }

    public AuthData login(String username, String password) throws DataAccessException {
        if (username == null || password == null) {
            throw new DataAccessException("Error: bad request");
        }
        UserData userData = dataAccess.getUserData(username);
        if (password.equals(userData.password())) {
            return dataAccess.createAuthData(username);
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public void logout(String authToken) throws DataAccessException {
        dataAccess.removeAuthData(authToken);
    }

    public int createGame(String authToken, String gameName) throws DataAccessException {
        if (!dataAccess.hasAuthToken(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        }
        if (gameName == null) {
            throw new DataAccessException("Error: bad request");
        }
        return dataAccess.createGame(gameName);
    }

    public Collection<GameData> getListGames(String authToken) throws DataAccessException {
        if (!dataAccess.hasAuthToken(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        }
        return dataAccess.getListGames();
    }

    public void joinGame(String authToken, String playerColor, int gameID) throws DataAccessException {
        if (!dataAccess.hasAuthToken(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        }
        String username = dataAccess.getUsername(authToken);
        if (playerColor != null &&
                (playerColor.equals("WHITE") || playerColor.equals("BLACK")) &&
                (gameID > 0)) {
            GameData game = dataAccess.getGame(String.format("%04d",gameID));
            if ((playerColor.equals("WHITE") && game.whiteUsername() != null) ||
                    (playerColor.equals("BLACK") && game.blackUsername() != null)) {
                throw new DataAccessException("Error: already taken");
            }
            GameData updatedGame;
            if (playerColor.equals("WHITE")) {
                updatedGame = new GameData(gameID, username, game.blackUsername(), game.gameName(), game.game());
            } else {
                updatedGame = new GameData(gameID, game.whiteUsername(), username, game.gameName(), game.game());
            }
            dataAccess.updateGame(String.format("%04d",gameID), updatedGame);
        } else {
            throw new DataAccessException("Error: bad request");
        }
    }
}
