package dataaccess;

import model.*;

import java.util.*;

public class MemoryDataAccess implements DataAccess {
    HashMap<String, UserData> userDataSet = new HashMap<String, UserData>();
    HashMap<String,GameData> gameDataSet = new HashMap<String, GameData>();
    HashMap<String, String> authDataSet = new HashMap<String, String>();

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public UserData getUserData(String username) throws DataAccessException {
        if (userDataSet.containsKey(username)) {
            return userDataSet.get(username);
        }
        throw new DataAccessException("unauthorized");
    }

    public AuthData createUser(UserData inputUserData) throws AlreadyTakenException {
        String inputUsername = inputUserData.username();
        if (userDataSet.containsKey(inputUsername)) {
            throw new AlreadyTakenException("username already taken");
        }
        userDataSet.put(inputUsername, inputUserData);
        //code below could be turned into its own method later
        return createAuthData(inputUsername);
    }

    public AuthData createAuthData(String username) {
        AuthData registerResult = new AuthData(generateToken(), username);
        authDataSet.put(registerResult.authToken(), registerResult.username());
        return registerResult;
    }

    public boolean verifyAuthData(String authToken) {
        return authDataSet.containsKey(authToken);
    }

    public void removeAuthData(String authToken) {
        authDataSet.remove(authToken);
    }

    public Collection<GameData> listGames() {
        return gameDataSet.values();
    }

    public void clearAuthData() {
        authDataSet.clear();
    }

    public void clearUserData() {
        userDataSet.clear();
    }

    public void clearGameData() {
        gameDataSet.clear();
    }

}
