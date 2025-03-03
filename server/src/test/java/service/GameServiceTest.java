package service;

import DOA.MemoryAuthDAO;
import DOA.MemoryGameDAO;
import DOA.MemoryUserDOA;
import Models.AuthData;
import Models.UserData;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameServiceTest {
    UserService.RegisterRequest user= new UserService.RegisterRequest("collin", "12345", "collin@gmail.com");

    static UserService userService;
    static MemoryAuthDAO authDAO;
    static MemoryUserDOA userDOA;
    static AuthData authData;
    static UserData userData;
    static MemoryGameDAO gameDAO;
    static GameService gameService;
    static String authToken;

    @BeforeEach
    void setup() throws DataAccessException {
        userDOA = new MemoryUserDOA();
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
    void CreateGameSuccess() throws DataAccessException {
        GameService.CreateRequest createRequest = new GameService.CreateRequest("gamename");

        Assertions.assertDoesNotThrow(() -> gameService.createGame(authToken, createRequest));
    }

    @Test
    void CreateGameFailure(){
        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () -> {
            GameService.CreateRequest createRequest = new GameService.CreateRequest("gamename");
            gameService.createGame("badAuthToken", createRequest);
        });
        Assertions.assertEquals("Invalid authToken", e.getMessage());

    }

    @Test
    void JoinGameSuccess() throws DataAccessException {
        GameService.CreateRequest createRequest = new GameService.CreateRequest("gamename");
        gameService.createGame(authToken, createRequest);

        GameService.JoinGameRequest joinGameRequest = new GameService.JoinGameRequest("WHITE", 1);
        Assertions.assertDoesNotThrow(() -> gameService.JoinGame(joinGameRequest ,authToken));

    }

    @Test
    void JoinGameFailure() throws DataAccessException {
        GameService.CreateRequest createRequest = new GameService.CreateRequest("gamename");
        gameService.createGame(authToken, createRequest);

        GameService.JoinGameRequest joinGameRequest = new GameService.JoinGameRequest("BLUE", 1);

        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.JoinGame(joinGameRequest ,authToken);
        });
        Assertions.assertEquals("not a valid color", e.getMessage());
    }

    @Test
    void ListGameSuccess() throws DataAccessException {
        GameService.CreateRequest createRequest = new GameService.CreateRequest("gamename");
        gameService.createGame(authToken, createRequest);

        Assertions.assertDoesNotThrow(() -> gameService.ListGames(authToken));
    }

    @Test
    void ListGameFailure() throws DataAccessException {
        GameService.CreateRequest createRequest = new GameService.CreateRequest("gamename");
        gameService.createGame(authToken, createRequest);

        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.ListGames("authToken");
        });
        Assertions.assertEquals("Invalid authToken", e.getMessage());
    }


}
