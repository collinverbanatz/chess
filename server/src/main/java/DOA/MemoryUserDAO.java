package DOA;


import models.UserData;
import dataaccess.DataAccessException;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UsrDAO {

    private static Map<String, UserData> allUserData = new HashMap<>();

    @Override
    public UserData getUser(String userName) throws DataAccessException {
        return allUserData.get(userName);
    }

    @Override
    public void putUser(UserData userData) {
        allUserData.put(userData.userName, userData);
    }

    public void clear(){
        allUserData.clear();
    }
}
