package service;

import dataaccess.DataAccess;
import model.*;

public class Service {

    private final DataAccess dataAccess;

    public Service(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public RegisterResult register(UserData userData) {
        return DataAccess.
    }
}
