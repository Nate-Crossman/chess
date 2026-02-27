package dataaccess;


import model.*;

public interface DataAccess {
    //List out things we'll need for data access, create, read, update, delete
    UserData getUserData(String username) throws DataAccessException;

    AuthData createUser(UserData userData) throws DataAccessException;
//
//    void removeUserData() throws DataAccessException;
}
