package DOA;

import Models.AuthData;

public interface AuthDOA {
    void putAuthToken(AuthData authToken);

    void clear();
}
