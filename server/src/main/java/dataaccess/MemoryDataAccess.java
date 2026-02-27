package dataaccess;

import model.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class MemoryDataAccess implements DataAccess {
    HashMap<String, UserData> userDataSet = new HashMap<String, UserData>();
    HashSet<GameData> gameDataSet;
    HashMap<String, String> authDataSet = new HashMap<String, String>();

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public UserData getUserData(String username) throws DataAccessException {
        UserData data = userDataSet.get(username);
        if (data == null) {
            throw new DataAccessException("User does not exist");
        }
        return data;
    }

    public AuthData createUser(UserData inputUserData) throws DataAccessException {
        String inputUsername = inputUserData.username();
        if (userDataSet.containsKey(inputUsername)) {
            throw new DataAccessException("Username already taken");
        }
        userDataSet.put(inputUsername, inputUserData);
        //code below could be turned into it's own method later
        AuthData registerResult = new AuthData(generateToken(), inputUsername);
        authDataSet.put(registerResult.authToken(), registerResult.username());
        return registerResult;
    }
}
