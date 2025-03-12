package dataaccess;

import dao.*;
import models.UserData;
import org.junit.jupiter.api.*;

public class SQLUserTests {

    static SQLUserdao userDao;


    @BeforeAll
    static void makeDataBase() throws DataAccessException {
//   creates one database that will be used for every test
        userDao = new SQLUserdao();
    }

    @BeforeEach
    void setup() {
        userDao.clear();
    }

    @Test
    void positivePutUserTest() {
        UserData user = new UserData("collin", "1234", "someemail@email.com");
        Assertions.assertDoesNotThrow(() -> userDao.putUser(user));
    }

    @Test
    void negativePutUserTest() throws DataAccessException {
        UserData user = new UserData("collin", "1234", "someemail@email.com");
        UserData user2 = new UserData(null, "1234", "someemail@email.com");
        userDao.putUser(user);
        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () -> {
            userDao.putUser(user2);
        });
        Assertions.assertEquals("Not a valid username", e.getMessage());
    }

    @Test
    void positiveGetUserTest() throws DataAccessException {
        UserData user = new UserData("collin", "1234", "someemail@email.com");
        userDao.putUser(user);

        String userName = "collin";
        Assertions.assertEquals("collin", userName);
        Assertions.assertDoesNotThrow(() -> userDao.getUser(userName));
    }

    @Test
    void negativeGetUserTest() throws DataAccessException {
        UserData user = new UserData("collin", "1234", "someemail@email.com");

        UserData userName = userDao.getUser(user.userName);
        Assertions.assertNull(userName);
    }

    @Test
    void clearTest() throws DataAccessException {
        UserData user = new UserData("collin", "1234", "someemail@email.com");
        userDao.putUser(user);

        userDao.clear();
        UserData userName = userDao.getUser(user.userName);
        Assertions.assertNull(userName);
    }
}
