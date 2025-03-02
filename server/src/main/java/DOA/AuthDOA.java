package DOA;

import Models.AuthData;

public interface AuthDOA {
    void putAuthToken(AuthData authToken);

    void clear();

    void removeAuthToken(String authData);

    boolean authTokenExists(String authToken);

    AuthData getAuthDataByToken(String authToken);
}
