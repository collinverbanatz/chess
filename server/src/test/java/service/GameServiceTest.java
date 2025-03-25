package service;

import dao.MemoryAuthdao;
import dao.Memorygamedao;
import dao.MemoryUserdao;
import models.*;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameServiceTest {
    RegisterRequest user= new RegisterRequest("collin", "12345", "collin@gmail.com");

    static UserService userService;
    static MemoryAuthdao authDAO;
    static MemoryUserdao userDOA;
    static AuthData authData;
    static UserData userData;
    static Memorygamedao gameDAO;
    static GameService gameService;
    static String authToken;

    @BeforeEach
    void setup() throws DataAccessException {
        userDOA = new MemoryUserdao();
        authDAO = new MemoryAuthdao();
        gameDAO = new Memorygamedao();
        userService = new UserService(userDOA, authDAO, gameDAO);
        gameService = new GameService(authDAO, gameDAO);
//        authData = new AuthData();
//        userData = new UserData();


        // Clear existing data to ensure a fresh start
        userDOA.clear();
        authDAO.clear();
        gameDAO.clear();

//        register, login
        RegisterRequest user= new RegisterRequest("collin", "12345", "collin@gmail.com");
        RegisterResult registerResult= userService.register(user);
        authToken = registerResult.getAuthToken();
    }

    @Test
    void createGameSuccess() throws DataAccessException {
        CreateRequest createRequest = new CreateRequest("gamename");

        Assertions.assertDoesNotThrow(() -> gameService.createGame(authToken, createRequest));
    }

    @Test
    void createGameFailure(){
        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () -> {
            CreateRequest createRequest = new CreateRequest("gamename");
            gameService.createGame("badAuthToken", createRequest);
        });
        Assertions.assertEquals("Invalid authToken", e.getMessage());

    }

    @Test
    void joinGameSuccess() throws DataAccessException {
        CreateRequest createRequest = new CreateRequest("gamename");
        gameService.createGame(authToken, createRequest);

        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", 1);
        Assertions.assertDoesNotThrow(() -> gameService.joinGame(joinGameRequest ,authToken));

    }

    @Test
    void joinGameFailure() throws DataAccessException {
        CreateRequest createRequest = new CreateRequest("gamename");
        gameService.createGame(authToken, createRequest);

        JoinGameRequest joinGameRequest = new JoinGameRequest("BLUE", 1);

        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.joinGame(joinGameRequest ,authToken);
        });
        Assertions.assertEquals("not a valid color", e.getMessage());
    }

    @Test
    void listGameSuccess() throws DataAccessException {
        CreateRequest createRequest = new CreateRequest("gamename");
        gameService.createGame(authToken, createRequest);

        Assertions.assertDoesNotThrow(() -> gameService.listGames(authToken));
    }

    @Test
    void listGameFailure() throws DataAccessException {
        CreateRequest createRequest = new CreateRequest("gamename");
        gameService.createGame(authToken, createRequest);

        DataAccessException e = Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.listGames("authToken");
        });
        Assertions.assertEquals("Invalid authToken", e.getMessage());
    }


}
