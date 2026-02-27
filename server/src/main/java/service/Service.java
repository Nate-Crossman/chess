package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.*;

public class Service {

    private final DataAccess dataAccess;

    public Service(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public RegisterResult register(UserData userData) {
        try {
        UserData data = dataAccess.getUserData(userData.username());

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
