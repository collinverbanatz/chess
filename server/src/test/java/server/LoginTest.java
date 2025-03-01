package server;

import DOA.*;
import Models.AuthData;
import Models.UserData;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import Service.UserService;
import passoff.model.TestAuthResult;
import passoff.model.TestUser;

public class LoginTest {

    static UserService userService;
    static MemoryAuthDAO authDAO;
    static MemoryUserDOA userDOA;
    static AuthData authData;
    static UserData userData;

    @BeforeEach
    void setup() {
        userDOA = new MemoryUserDOA();
        authDAO = new MemoryAuthDAO();
        userService = new UserService();
//        authData = new AuthData();
//        userData = new UserData();


        // Clear existing data to ensure a fresh start
        userDOA.clear();
        authDAO.clear();
    }


    @Test
    void registerSuccessTest() throws DataAccessException {
        UserService.RegisterRequest user= new UserService.RegisterRequest("collin", "12345", "collin@gmail.com");
        UserService.RegisterResult result = userService.register(user);

//        Assertions.assertEquals(result, userDOA.getUser(user.getUsername()));
//        assertnotnull
        Assertions.assertNotNull(userDOA.getUser(user.getUsername()));
    }

}
