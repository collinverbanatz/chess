package DOA;

import models.UserData;
import dataaccess.DataAccessException;

public interface Usrdao {
    UserData getUser(String userName) throws DataAccessException;
    void putUser(UserData userData);

    void clear();
}
