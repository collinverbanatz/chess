package DOA;

import Models.UserData;
import dataaccess.DataAccessException;

public interface UsrDOA {
    UserData getUser(String userName) throws DataAccessException;
    void putUser(UserData userData);

    void clear();
}
