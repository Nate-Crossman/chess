package service;

import chess.ChessGame;
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
        if (!userData.isValidUserData()) {
            throw new BadRequestException("bad request");
        }
        return dataAccess.createUser(userData);

    }

    public AuthData login(LoginRequest loginRequest) throws DataAccessException, BadRequestException {
        if (!loginRequest.isValidLoginRequest()) {
            throw new BadRequestException("bad request");
        }
        UserData userData = dataAccess.getUserData(loginRequest.username());
        if (!isValidPassword(loginRequest, userData)) {
            throw new DataAccessException("unauthorized");
        }
        return dataAccess.createAuthData(loginRequest.username());

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
        authorizationCheck(authToken);
        return dataAccess.listGames();
    }

    public int createGame(String authToken, CreateGameRequest request) throws DataAccessException, BadRequestException {
        authorizationCheck(authToken);
        createGameRequestCheck(request);
        return dataAccess.createGame(request.gameName());
    }

    private void createGameRequestCheck(CreateGameRequest request) throws BadRequestException {
        if (!request.isValidGameRequest()) {
            throw new BadRequestException("bad request");
        }
    }

    public void joinGame(String authToken, JoinGameRequest request)
            throws DataAccessException,
            BadRequestException,
            AlreadyTakenException {
        authorizationCheck(authToken);
        joinGameRequestCheck(request);
        String username = dataAccess.getUsername(authToken);
        GameData game = dataAccess.getGame(request.gameID());
        game = attemptJoinGame(request, game, username);
        dataAccess.updateGame(request.gameID(), game);


    }

    private void joinGameRequestCheck(JoinGameRequest request) throws BadRequestException {
        if (!request.isValidJoinGameRequest()) {
            throw new BadRequestException("bad request");
        }
    }

    private GameData attemptJoinGame(JoinGameRequest request, GameData game, String username)
            throws BadRequestException, AlreadyTakenException {
        GameData updatedGame;
        if (game == null) {
            throw new BadRequestException("bad request");
        }
        if (request.playerColor().equals("WHITE")) {
            if (game.whiteUsername() != null) {
                throw new AlreadyTakenException("already taken");
            }
            updatedGame = game.addWhitePlayer(username);
        } else {
            if (game.blackUsername() != null) {
                throw  new AlreadyTakenException("already taken");
            }
            updatedGame = game.addBlackPlayer(username);
        }
        return updatedGame;
    }

    private void authorizationCheck(String authToken) throws DataAccessException {
        if (!dataAccess.verifyAuthData(authToken)) {
            throw new DataAccessException("unauthorized");
        }
    }



    public void clear() {
        try {
            dataAccess.clearAuthData();
            dataAccess.clearUserData();
            dataAccess.clearGameData();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
