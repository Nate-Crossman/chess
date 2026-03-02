package dataaccess;


import model.*;

import java.util.Collection;

public interface DataAccess {
    //List out things we'll need for data access, create, read, update, delete
    UserData getUserData(String username) throws DataAccessException;

    AuthData createUser(UserData userData) throws AlreadyTakenException;
//
    AuthData createAuthData(String username);

    boolean verifyAuthData(String authToken);
//
    void removeAuthData(String authToken);

    Collection<GameData> listGames();

    int createGame(String gameName);

    GameData getGame(int gameID);

    void updateGame(int gameID, GameData gameData);

    String getUsername(String authToken);

    void clearAuthData();

    void clearUserData();

    void clearGameData();


}
