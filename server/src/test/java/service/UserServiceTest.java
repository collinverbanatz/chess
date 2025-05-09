package service;

import dao.*;
import models.*;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;

public class UserServiceTest {
    RegisterRequest user= new RegisterRequest("collin", "12345", "collin@gmail.com");

    static UserService userService;
    static MemoryAuthdao authDAO;
    static MemoryUserdao userDOA;
    static AuthData authData;
    static UserData userData;
    static Memorygamedao gameDAO;

    @BeforeEach
    void setup() {
        userDOA = new MemoryUserdao();
        authDAO = new MemoryAuthdao();
        gameDAO = new Memorygamedao();
        userService = new UserService(userDOA, authDAO, gameDAO);
//        authData = new AuthData();
//        userData = new UserData();


        // Clear existing data to ensure a fresh start
        userDOA.clear();
        authDAO.clear();
        gameDAO.clear();
    }


    @Test
    void registerSuccessTest() throws DataAccessException {
        RegisterRequest user= new RegisterRequest("collin", "12345", "collin@gmail.com");
        RegisterResult result = userService.register(user);


        Assertions.assertNotNull(userDOA.getUser(user.getUsername()));
    }

    @Test
    void registerFailTest() throws DataAccessException {
        RegisterRequest user = new RegisterRequest("collin", "12345", "collin@gmail.com");
        userService.register(user);

        RegisterRequest user2 = new RegisterRequest("collin", "12345", "collin@gmail.com");
        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () -> {
            userService.register(user2);
        });
        Assertions.assertEquals("username all ready exists", e.getMessage());
    }

    @Test
    void loginSuccessTest() throws DataAccessException {
        userService.register(user);
        LoginRequest loginRequest = new LoginRequest("collin", "12345");
        RegisterResult loginResult = userService.login(loginRequest);

        Assertions.assertNotNull(loginResult.getAuthToken(), "Auth token should be returned on successful login.");
    }

    @Test
    void loginFailureTest(){
        LoginRequest user1 = new LoginRequest("collin", "12345");

        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () -> {
            userService.login(user1);
        });
        Assertions.assertEquals("user name doesn't exist", e.getMessage());
    }

    @Test
    void logoutSuccessTest() throws DataAccessException {
        RegisterResult registerResult = userService.register(user);
        LoginRequest loginRequest = new LoginRequest("collin", "12345");
        RegisterResult loginResult = userService.login(loginRequest);
        userService.logout(loginResult.getAuthToken());
        boolean result = authDAO.authTokenExists(loginResult.getAuthToken());

        Assertions.assertFalse(result);
    }

    @Test
    void logoutFailTest() {

        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () -> {
            userService.logout("badToken");
        });
        Assertions.assertEquals("Invalid authToken", e.getMessage());
    }

    @Test
    void clearTest() throws DataAccessException {
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
