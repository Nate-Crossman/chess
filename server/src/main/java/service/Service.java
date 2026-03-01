package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.*;

public class Service {

    private final DataAccess dataAccess;

    public Service(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public AuthData register(UserData userData) {
        try {
        return dataAccess.createUser(userData);
        } catch (DataAccessException e) {
            return null;
        }
    }

    public void clear() {
        dataAccess.clearAuthData();
        dataAccess.clearUserData();
        dataAccess.clearGameData();
    }
}
