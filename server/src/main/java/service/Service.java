package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

public class Service {

    private final DataAccess dataAccess;

    public Service(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void clear() {
        dataAccess.clearData();
    }

    public AuthData register(UserData inputData) throws DataAccessException {
        if (inputData.username() == null ||
                inputData.password() == null ||
                inputData.email() == null) {
            throw new DataAccessException("Error: bad request");
        }
        return dataAccess.addUserData(inputData);

    }
}
