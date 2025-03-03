package DOA;

import models.AuthData;

public interface AuthDAO {
    void putAuthToken(AuthData authToken);

    void clear();

    void removeAuthToken(String authData);

    boolean authTokenExists(String authToken);

    AuthData getAuthDataByToken(String authToken);
}
