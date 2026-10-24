package dataaccess;

import model.*;

import java.util.HashMap;

public class MemoryDataAccess implements DataAccess {
//    Username --> UserData
    HashMap<String, UserData> userDataHashMap = new HashMap<String, UserData>();
//    Gamename --> GameData
    HashMap<String, GameData> gameDataHashMap = new HashMap<String, GameData>();
//    authToken --> userName
    HashMap<String, String> authDataHashMap = new HashMap<String, String>();

    public void clearData() {
        userDataHashMap.clear();
        gameDataHashMap.clear();
        authDataHashMap.clear();
    }
}
