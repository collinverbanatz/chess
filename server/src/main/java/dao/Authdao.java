package dao;

import dataaccess.DataAccessException;
import models.AuthData;

public interface Authdao {
    void putAuthToken(AuthData authToken) throws DataAccessException;

    void clear();

    void removeAuthToken(String authData) throws DataAccessException;

    boolean authTokenExists(String authToken) throws DataAccessException;

    AuthData getAuthDataByToken(String authToken) throws DataAccessException;
}
