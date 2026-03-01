package dataaccess;

import model.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class MemoryDataAccess implements DataAccess {
    HashMap<String, UserData> userDataSet = new HashMap<String, UserData>();
    HashMap<String,GameData> gameDataSet = new HashMap<String, GameData>();
    HashMap<String, String> authDataSet = new HashMap<String, String>();

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public UserData getUserData(String username) throws DataAccessException {
        return userDataSet.get(username);

    }

    public AuthData createUser(UserData inputUserData) throws DataAccessException {
        String inputUsername = inputUserData.username();
        if (userDataSet.containsKey(inputUsername)) {
            throw new DataAccessException("username already taken");
        }
        userDataSet.put(inputUsername, inputUserData);
        //code below could be turned into its own method later
        AuthData registerResult = new AuthData(generateToken(), inputUsername);
        authDataSet.put(registerResult.authToken(), registerResult.username());
        return registerResult;
    }

    public boolean verifyAuthData(AuthData authData) {
        return false;
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
