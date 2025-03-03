package DOA;

import models.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {

    private static Map<String, AuthData> allAuthData = new HashMap<>();

    @Override
    public void putAuthToken(AuthData authToken) {
        allAuthData.put(authToken.authToken, authToken);
    }

    @Override
    public void clear() {
        allAuthData.clear();
    }

    @Override
    public void removeAuthToken(String authData) {
        allAuthData.remove(authData);
    }

    @Override
    public boolean authTokenExists(String authToken) {
        System.out.println("Storing authToken: " + authToken); // Debugging output

        return allAuthData.containsKey(authToken);
    }

    @Override
    public AuthData getAuthDataByToken(String authToken) {
        return allAuthData.get(authToken);
    }
}
