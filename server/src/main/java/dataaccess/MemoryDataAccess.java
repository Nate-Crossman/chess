package dataaccess;

import model.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class MemoryDataAccess implements DataAccess {
    HashMap<String, UserData> userDataSet = new HashMap<String, UserData>();
    HashSet<GameData> gameDataSet;
    HashSet<AuthData> authDataSet;

    public UserData getUserData(String username) throws DataAccessException {
        UserData data = userDataSet.get(username);
        if (data == null) {
            throw new DataAccessException("User does not exist");
        }
        return data;
    }
}
