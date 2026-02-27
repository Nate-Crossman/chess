package dataaccess;

import model.*;

import java.util.HashSet;

public class MemoryDataAccess implements DataAccess {
    HashSet<UserData> userDataSet;
    HashSet<GameData> gameDataSet;
    HashSet<AuthData> authDataSet;


    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }
}
