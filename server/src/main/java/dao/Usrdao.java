package dao;

import models.UserData;
import dataaccess.DataAccessException;

public interface Usrdao {
    UserData getUser(String userName) throws DataAccessException;
    void putUser(UserData userData) throws DataAccessException;

    void clear();

    String hashPassword(String password);
}
