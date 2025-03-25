package client;

import org.junit.jupiter.api.*;
import server.Server;
import models.RegisterResult;

import java.io.IOException;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clearServer() throws IOException {
        serverFacade.clear();
    }


    @Test
    void registerSuccessTest() throws IOException {
        RegisterResult authData = serverFacade.register("testUser", "password", "test@example.com");
        Assertions.assertNotNull(authData);
        Assertions.assertTrue(authData.getAuthToken().length() > 10);
    }

    @Test
    void registerFailTest() throws IOException {
        serverFacade.register("testUser", "password", "test@example.com");
        Assertions.assertThrows(IOException.class, () -> {serverFacade.register("testUser", "password", "test@example.com");
        });
    }

    @Test
    void loginSuccessTest() throws IOException {
        serverFacade.register("testUser", "password", "fakeemail");
        var authData = serverFacade.login("testUser", "password");
        Assertions.assertNotNull(authData);
    }

    @Test
    void loginFailTest() throws IOException {
        serverFacade.register("testUser", "password", "fakeemail");
        Assertions.assertThrows(IOException.class, () -> {serverFacade.login("testUser", "notGood");
        });
    }

    @Test
    void createGameSuccessTest() throws IOException {
        RegisterResult registerResult = serverFacade.register("testUser", "password", "fakeemail");
        var authData = serverFacade.createGame(registerResult.getAuthToken(), "testGame");
        Assertions.assertNotNull(authData);
    }

    @Test
    void createGameFailTest() throws IOException {
        RegisterResult registerResult = serverFacade.register("testUser", "password", "fakeemail");
        Assertions.assertThrows(IOException.class, () -> {serverFacade.createGame("badAuthToken", "testGame");
        });
    }

    @Test
    void listGamesSuccessTest() throws IOException {
        RegisterResult registerResult = serverFacade.register("testUser", "password", "fakeemail");
        var authData = serverFacade.listGame(registerResult.getAuthToken());
        Assertions.assertNotNull(authData);
    }

    @Test
    void listGamesFailTest() throws IOException {
        RegisterResult registerResult = serverFacade.register("testUser", "password", "fakeemail");
        Assertions.assertThrows(IOException.class, () -> {serverFacade.listGame("badAuthToken");
        });
    }

    @Test
    void joinGameSuccessTest() throws IOException {
        RegisterResult registerResult = serverFacade.register("testUser", "password", "fakeemail");
        var gameRessult = serverFacade.createGame(registerResult.getAuthToken(), "testGame");
        serverFacade.joinGame(registerResult.getAuthToken(), gameRessult.getGameID(), "WHITE");
        Assertions.assertEquals(1, gameRessult.getGameID());
    }

    @Test
    void joinGameFailTest() throws IOException {
        RegisterResult registerResult = serverFacade.register("testUser", "password", "fakeemail");
        var gameRessult = serverFacade.createGame(registerResult.getAuthToken(), "testGame");
        Assertions.assertThrows(IOException.class, () -> {serverFacade.joinGame("", gameRessult.getGameID(), "blue");
        });
    }

    @Test
    void logoutSuccessTest() throws IOException {
        RegisterResult registerResult = serverFacade.register("testUser", "password", "fakeemail");
        serverFacade.logout(registerResult.getAuthToken());
        Assertions.assertThrows(IOException.class, () -> {serverFacade.listGame(registerResult.getAuthToken());
        });
    }

    @Test
    void logoutFailTest() throws IOException {
        RegisterResult registerResult = serverFacade.register("testUser", "password", "fakeemail");
        Assertions.assertThrows(IOException.class, () -> {serverFacade.logout("");
        });
    }

    @Test
    void clear() throws IOException {
        RegisterResult registerResult = serverFacade.register("testUser", "password", "fakeemail");
        serverFacade.clear();
        Assertions.assertThrows(IOException.class, () -> {serverFacade.listGame(registerResult.getAuthToken());
        });
    }
}
