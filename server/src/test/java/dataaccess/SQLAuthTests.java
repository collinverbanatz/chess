package dataaccess;

import dao.*;
import models.AuthData;
import org.junit.jupiter.api.*;

public class SQLAuthTests {

    static SQLAuthdao authDao;

    @BeforeAll
    static void makeDataBase() throws DataAccessException {
//   creates one database that will be used for every test
        authDao = new SQLAuthdao();
    }

    @BeforeEach
    void setup() {
        authDao.clear();
    }

    @Test
    void positivePutAuthToken() throws DataAccessException {
        AuthData authData = new AuthData("authTokenGood", "collin");

        authDao.putAuthToken(authData);
        AuthData authToken = authDao.getAuthDataByToken("authTokenGood");
        Assertions.assertNotNull(authToken);
        Assertions.assertEquals(authData.authToken, authToken.getAuthToken());
    }

    @Test
    void negativePutAuthToken() throws DataAccessException {
        AuthData authData = new AuthData("authTokenBad", "collin");

        authDao.putAuthToken(authData);
        AuthData authdata = authDao.getAuthDataByToken("doesn't exist token");
        Assertions.assertNull(authdata);
    }

    @Test
    void positiveClear() throws DataAccessException {
        AuthData authData = new AuthData("authTokenGood", "collin");
        authDao.putAuthToken(authData);

        authDao.clear();
        AuthData received = authDao.getAuthDataByToken(authData.getAuthToken());
        Assertions.assertNull(received);
    }

    @Test
    void positiveRemoveAuthToken() throws DataAccessException {
        AuthData authData = new AuthData("authTokenGood", "collin");

        authDao.putAuthToken(authData);
        authDao.removeAuthToken(authData.getAuthToken());
        Assertions.assertFalse(authDao.authTokenExists("authTokenGood"));
    }

    @Test
    void negativeRemoveAuthToken() throws DataAccessException {
        AuthData authData = new AuthData("authTokenBad", "collin");

        authDao.putAuthToken(authData);
        Assertions.assertDoesNotThrow(() -> authDao.removeAuthToken("doesn't exist"));
    }

    @Test
    void positiveAuthTokenExists() throws DataAccessException {
        AuthData authData = new AuthData("authTokenGood", "collin");

        authDao.putAuthToken(authData);
        boolean result = authDao.authTokenExists("authTokenGood");
        Assertions.assertTrue(result);
    }

    @Test
    void negativeAuthTokenExists() throws DataAccessException {
        boolean result = authDao.authTokenExists("authTokenBad");
        Assertions.assertFalse(result);
    }

    @Test
    void positivistAuthDataByToken() throws DataAccessException {
        AuthData authData = new AuthData("authTokenGood", "collin");

        authDao.putAuthToken(authData);
        AuthData result =authDao.getAuthDataByToken("authTokenGood");
        Assertions.assertNotNull(result);
    }

    @Test
    void negativeGetAuthDataByToken() throws DataAccessException {
        AuthData result =authDao.getAuthDataByToken("authTokenGood");
        Assertions.assertNull(result);

    }

}
