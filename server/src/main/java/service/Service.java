package service;

import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.*;

public class Service {

    private final DataAccess dataAccess;

    public Service(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public AuthData register(UserData userData) throws AlreadyTakenException {
//        try {
        return dataAccess.createUser(userData);
//        } catch (DataAccessException e) {
//            return null;
//        }
    }

    public AuthData login(LoginRequest loginRequest) throws DataAccessException, BadRequestException {
        UserData userData = dataAccess.getUserData(loginRequest.username());
        if (isValidPassword(loginRequest, userData)) {
            return dataAccess.createAuthData(loginRequest.username());
        }
        throw new DataAccessException("unauthorized");
    }

    private boolean isValidPassword(LoginRequest request, UserData data) {
        return (data.username().equals(request.username()) && data.password().equals(request.password()));
    }

    public void clear() {
        dataAccess.clearAuthData();
        dataAccess.clearUserData();
        dataAccess.clearGameData();
    }
}
