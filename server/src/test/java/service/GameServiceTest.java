package service;

import DOA.MemoryAuthDAO;
import DOA.MemoryGameDAO;
import DOA.MemoryUserDAO;
import models.AuthData;
import models.UserData;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameServiceTest {
    UserService.RegisterRequest user= new UserService.RegisterRequest("collin", "12345", "collin@gmail.com");

    static UserService userService;
    static MemoryAuthDAO authDAO;
    static MemoryUserDAO userDOA;
    static AuthData authData;
    static UserData userData;
    static MemoryGameDAO gameDAO;
    static GameService gameService;
    static String authToken;

    @BeforeEach
    void setup() throws DataAccessException {
        userDOA = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        userService = new UserService(userDOA, authDAO, gameDAO);
        gameService = new GameService(authDAO, gameDAO);
//        authData = new AuthData();
//        userData = new UserData();


        // Clear existing data to ensure a fresh start
        userDOA.clear();
        authDAO.clear();
        gameDAO.clear();

//        register, login
        UserService.RegisterRequest user= new UserService.RegisterRequest("collin", "12345", "collin@gmail.com");
        UserService.RegisterResult registerResult= userService.register(user);
        authToken = registerResult.getAuthToken();
    }

    @Test
    void createGameSuccess() throws DataAccessException {
        GameService.CreateRequest createRequest = new GameService.CreateRequest("gamename");

        Assertions.assertDoesNotThrow(() -> gameService.createGame(authToken, createRequest));
    }

    @Test
    void createGameFailure(){
        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () -> {
            GameService.CreateRequest createRequest = new GameService.CreateRequest("gamename");
            gameService.createGame("badAuthToken", createRequest);
        });
        Assertions.assertEquals("Invalid authToken", e.getMessage());

    }

    @Test
    void joinGameSuccess() throws DataAccessException {
        GameService.CreateRequest createRequest = new GameService.CreateRequest("gamename");
        gameService.createGame(authToken, createRequest);

        GameService.JoinGameRequest joinGameRequest = new GameService.JoinGameRequest("WHITE", 1);
        Assertions.assertDoesNotThrow(() -> gameService.joinGame(joinGameRequest ,authToken));

    }

    @Test
    void joinGameFailure() throws DataAccessException {
        GameService.CreateRequest createRequest = new GameService.CreateRequest("gamename");
        gameService.createGame(authToken, createRequest);

        GameService.JoinGameRequest joinGameRequest = new GameService.JoinGameRequest("BLUE", 1);

        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.joinGame(joinGameRequest ,authToken);
        });
        Assertions.assertEquals("not a valid color", e.getMessage());
    }

    @Test
    void listGameSuccess() throws DataAccessException {
        GameService.CreateRequest createRequest = new GameService.CreateRequest("gamename");
        gameService.createGame(authToken, createRequest);

        Assertions.assertDoesNotThrow(() -> gameService.listGames(authToken));
    }

    @Test
    void listGameFailure() throws DataAccessException {
        GameService.CreateRequest createRequest = new GameService.CreateRequest("gamename");
        gameService.createGame(authToken, createRequest);

        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.listGames("authToken");
        });
        Assertions.assertEquals("Invalid authToken", e.getMessage());
    }


}
