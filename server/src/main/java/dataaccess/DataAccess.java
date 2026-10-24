package dataaccess;

import model.AuthData;
import model.UserData;

public interface DataAccess {

    AuthData addUserData(UserData inputData) throws DataAccessException;

    AuthData createAuthData(String username);

    UserData getUserData(String username) throws DataAccessException;

    void clearData();
}
