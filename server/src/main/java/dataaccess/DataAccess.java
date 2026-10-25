package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;

public interface DataAccess {

    AuthData addUserData(UserData inputData) throws DataAccessException;

    AuthData createAuthData(String username);

    UserData getUserData(String username) throws DataAccessException;

    void removeAuthData(String authToken) throws DataAccessException;

    boolean hasAuthToken(String authToken);

    int createGame(String gameName) throws DataAccessException;

    Collection<GameData> getListGames();

    GameData getGame(String gameID) throws DataAccessException;

    void updateGame(String gameID, GameData updatedGame) throws DataAccessException;

    void clearData();

    String getUsername(String authToken) throws DataAccessException;

}
