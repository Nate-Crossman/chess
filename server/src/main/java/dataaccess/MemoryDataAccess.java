package dataaccess;

import model.*;

import java.util.HashMap;
import java.util.UUID;

public class MemoryDataAccess implements DataAccess {
//    Username --> UserData
    HashMap<String, UserData> userDataHashMap = new HashMap<String, UserData>();
//    Gamename --> GameData
    HashMap<String, GameData> gameDataHashMap = new HashMap<String, GameData>();
//    authToken --> userName
    HashMap<String, String> authDataHashMap = new HashMap<String, String>();


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

    public void clearData() {
        userDataHashMap.clear();
        gameDataHashMap.clear();
        authDataHashMap.clear();
    }
}
