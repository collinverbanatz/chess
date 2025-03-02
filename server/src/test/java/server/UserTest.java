package server;

import DOA.*;
import Models.AuthData;
import Models.UserData;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import Service.UserService;

public class UserTest {
    UserService.RegisterRequest user= new UserService.RegisterRequest("collin", "12345", "collin@gmail.com");

    static UserService userService;
    static MemoryAuthDAO authDAO;
    static MemoryUserDOA userDOA;
    static AuthData authData;
    static UserData userData;

    @BeforeEach
    void setup() {
        userDOA = new MemoryUserDOA();
        authDAO = new MemoryAuthDAO();
        userService = new UserService(userDOA, authDAO);
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


        Assertions.assertNotNull(userDOA.getUser(user.getUsername()));
    }

    @Test
    void registerFailTest() throws DataAccessException {
        try {
            UserService.RegisterRequest user = new UserService.RegisterRequest("collin", "12345", "collin@gmail.com");
            UserService.RegisterResult result = userService.register(user);

            UserService.RegisterRequest user2 = new UserService.RegisterRequest("collin", "12345", "collin@gmail.com");


            Assertions.assertNull(result.getAuthToken());
        }
        catch (DataAccessException e){
            Assertions.assertTrue(e.getMessage().contains("username all ready exists"));
        }
    }

    @Test
    void LoginSuccessTest() throws DataAccessException {
        userService.register(user);
        UserService.LoginRequest loginRequest = new UserService.LoginRequest("collin", "12345");
        UserService.LoginResult loginResult = userService.login(loginRequest);

        Assertions.assertNotNull(loginResult.getAuthToken(), "Auth token should be returned on successful login.");
    }

    @Test
    void LoginFailureTest(){
        Assertions.assertEquals(1,0);
    }

    @Test
    void LogoutSuccessTest() throws DataAccessException {
        UserService.RegisterResult registerResult = userService.register(user);
        UserService.LoginRequest loginRequest = new UserService.LoginRequest("collin", "12345");
        UserService.LoginResult loginResult = userService.login(loginRequest);
        userService.logout(loginResult.getAuthToken());
        boolean result = authDAO.authTokenExists(loginResult.getAuthToken());

        Assertions.assertFalse(result);
    }

    @Test
    void logoutFailTest() {
        Assertions.assertEquals(1,0);

    }

    @Test
    void ClearTest() throws DataAccessException {
//        checks to see if it registers correctly
        Assertions.assertDoesNotThrow(() -> userService.register(user));

//        chekc to see if user was added
        Assertions.assertNotNull(userDOA.getUser("collin"));

//        clear from database
        Assertions.assertDoesNotThrow(() -> userService.clear());

//        check to see if its in database
        Assertions.assertNull(userDOA.getUser("collin"));

    }

}
