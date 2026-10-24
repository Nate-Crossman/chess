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
}
