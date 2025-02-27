package DOA;


import Models.UserData;
import dataaccess.DataAccessException;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDOA implements UsrDOA{

    @Override
    public UserData getUser(String userName) throws DataAccessException {
        if(AllUserData.containsKey(userName)){
            UserData userData = AllUserData.get(userName);
            return userData;
        }
        else{
            throw new DataAccessException("no user with that user name.");
        }
    }

    private Map<String, UserData> AllUserData = new HashMap<>();
}
