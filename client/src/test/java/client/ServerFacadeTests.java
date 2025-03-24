package client;

import net.ServerFacade;
import org.junit.jupiter.api.*;
import server.Server;
import service.UserService;

import java.io.IOException;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade serverFacade;

    public ServerFacadeTests() throws IOException {
    }


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade();
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
        UserService.RegisterResult authData = serverFacade.register("testUser", "password", "test@example.com");
        Assertions.assertNotNull(authData);
        Assertions.assertTrue(authData.getAuthToken().length() > 10);
    }

    @Test
    void registerFailTest() throws IOException {
        serverFacade.register("testUser", "password", "test@example.com");
        Assertions.assertThrows(IOException.class, () -> {serverFacade.register("testUser", "password", "test@example.com");
        });
    }

}
