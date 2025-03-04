package dao;

import models.AuthData;

public interface Authdao {
    void putAuthToken(AuthData authToken);

    void clear();

    void removeAuthToken(String authData);

    boolean authTokenExists(String authToken);

    AuthData getAuthDataByToken(String authToken);
}
