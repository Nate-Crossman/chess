package dataaccess;

import chess.ChessGame;
import model.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MemoryDataAccess implements DataAccess {
//    Username --> UserData
    HashMap<String, UserData> userDataHashMap = new HashMap<String, UserData>();
//    Gamename --> GameData
    HashMap<String, GameData> gameDataHashMap = new HashMap<String, GameData>();
//    authToken --> userName
    HashMap<String, String> authDataHashMap = new HashMap<String, String>();

    int idCounter = 1;


    public UserData getUserData(String username) throws DataAccessException {
        if (userDataHashMap.containsKey(username)) {
            return userDataHashMap.get(username);
        }
        throw new DataAccessException("Error: unauthorized");
    }

    public AuthData addUserData(UserData inputData) throws DataAccessException {
        String username = inputData.username();
        if (userDataHashMap.containsKey(username)) {
            throw new DataAccessException("Error: already taken");
        }
        userDataHashMap.put(username, inputData);
        return createAuthData(username);
    }

    public AuthData createAuthData(String username) {
        String token = UUID.randomUUID().toString();
        authDataHashMap.put(token, username);
        return new AuthData(token, username);
    }

    public void removeAuthData(String authToken) throws DataAccessException {
        if (hasAuthToken(authToken)) {
            authDataHashMap.remove(authToken);
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public boolean hasAuthToken(String authToken) {
        return authDataHashMap.containsKey(authToken);
    }

    public int createGame(String gameName) {
        int id = idCounter;
        idCounter++;
        GameData data = new GameData(id, null, null, gameName, new ChessGame());
        gameDataHashMap.put(String.format("%04d",id), data);
        return id;
    }

    public Collection<GameData> getListGames() {
        return gameDataHashMap.values();
    }

    public GameData getGame(String gameID) throws DataAccessException {
        if (gameDataHashMap.containsKey(gameID)) {
            return gameDataHashMap.get(gameID);
        } else {
            throw new DataAccessException("Error: bad request");
        }
    }

    public String getUsername(String authToken) throws DataAccessException {
        if (authDataHashMap.containsKey(authToken)) {
            return authDataHashMap.get(authToken);
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public void updateGame(String gameID, GameData updatedGame){
        gameDataHashMap.remove(gameID);
        gameDataHashMap.put(gameID, updatedGame);
    }

    public void clearData() {
        userDataHashMap.clear();
        gameDataHashMap.clear();
        authDataHashMap.clear();
        idCounter = 1;
    }
}
