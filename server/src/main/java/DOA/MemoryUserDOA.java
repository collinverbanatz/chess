package DOA;


import Models.UserData;
import dataaccess.DataAccessException;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDOA implements UsrDOA{

    private static Map<String, UserData> AllUserData = new HashMap<>();

    @Override
    public UserData getUser(String userName) throws DataAccessException {
        return AllUserData.get(userName);
    }

    @Override
    public void putUser(UserData userData) {
        AllUserData.put(userData.userName, userData);
    }

    public void clear(){
        AllUserData.clear();
    }
}
