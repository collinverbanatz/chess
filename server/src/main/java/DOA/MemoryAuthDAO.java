package DOA;

import Models.AuthData;
import Models.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDOA{
    @Override
    public void putAuthToken(AuthData authToken) {
        allAuthData.put(authToken.authToken, authToken);
    }
    private Map<String, AuthData> allAuthData = new HashMap<>();
}
