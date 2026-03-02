package service;

import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.*;

import java.util.Collection;

public class Service {

    private final DataAccess dataAccess;

    public Service(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public AuthData register(UserData userData) throws AlreadyTakenException, BadRequestException {
//        try {
        return dataAccess.createUser(userData);
//        } catch (DataAccessException e) {
//            return null;
//        }
    }

    public AuthData login(LoginRequest loginRequest) throws DataAccessException, BadRequestException {
        UserData userData = dataAccess.getUserData(loginRequest.username());
        if (isValidPassword(loginRequest, userData)) {
            return dataAccess.createAuthData(loginRequest.username());
        }
        throw new DataAccessException("unauthorized");
    }

    private boolean isValidPassword(LoginRequest request, UserData data) {
        return (data.username().equals(request.username()) && data.password().equals(request.password()));
    }

    public void logout(String authToken) throws DataAccessException {
        if (dataAccess.verifyAuthData(authToken)) {
            dataAccess.removeAuthData(authToken);
        } else {
            throw new DataAccessException("unauthorized");
        }
    }

    public Collection<GameData> listGames(String authToken) throws DataAccessException, BadRequestException {
        if (dataAccess.verifyAuthData(authToken)) {
            return dataAccess.listGames();
        }
        throw new DataAccessException("unauthorized");
    }

    public int createGame(String authToken, CreateGameRequest request) throws DataAccessException, BadRequestException {
        authorizationCheck(authToken);
        createGameRequestCheck(request);
        return dataAccess.createGame(request.gameName());
    }

    private void authorizationCheck(String authToken) throws DataAccessException {
        if (!dataAccess.verifyAuthData(authToken)) {
            throw new DataAccessException("unauthorized");
        }
    }

    private void createGameRequestCheck(CreateGameRequest request) throws BadRequestException {
        if (!request.isValidGameRequest()) {
            throw new BadRequestException("bad request");
        }
    }

    public void clear() {
        dataAccess.clearAuthData();
        dataAccess.clearUserData();
        dataAccess.clearGameData();
    }
}
