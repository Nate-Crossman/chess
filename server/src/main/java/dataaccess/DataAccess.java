package dataaccess;


import model.*;

public interface DataAccess {
    //List out things we'll need for data access, create, read, update, delete
    UserData getUserData(String username) throws DataAccessException;

    AuthData createUser(UserData userData) throws AlreadyTakenException;
//
    AuthData createAuthData(String username);

    boolean verifyAuthData(String authToken);
//
    void removeAuthData(String authToken);

    void clearAuthData();

    void clearUserData();

    void clearGameData();


}
