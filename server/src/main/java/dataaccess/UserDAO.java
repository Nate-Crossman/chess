package dataaccess;

import model.*;

public interface UserDAO {

    UserData getUser(String username) throws DataAccessException;

    void createUser(UserData userData) throws DataAccessException;

    void removeUserData() throws DataAccessException;

}
