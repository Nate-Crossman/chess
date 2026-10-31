package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.UUID;

public class MySQLDataAccess implements DataAccess {

    public UserData getUserData(String username) throws DataAccessException {
        return null;
    }

    public AuthData addUserData(UserData inputData) throws DataAccessException {
        return null;
    }

    public AuthData createAuthData(String username) {
        return null;
    }

    public void removeAuthData(String authToken) throws DataAccessException {

    }

    public boolean hasAuthToken(String authToken) {
        return false;
    }

    public int createGame(String gameName) {
        return 0;
    }

    public Collection<GameData> getListGames() {
        return null;
    }

    public GameData getGame(String gameID) throws DataAccessException {
        return null;
    }

    public String getUsername(String authToken) throws DataAccessException {
        return null;
    }

    public void updateGame(String gameID, GameData updatedGame){

    }

    public void clearData() {

    }
}
