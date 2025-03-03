package DOA;

import models.UserData;
import dataaccess.DataAccessException;

public interface UsrDAO {
    UserData getUser(String userName) throws DataAccessException;
    void putUser(UserData userData);

    void clear();
}
