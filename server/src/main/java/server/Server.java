package server;

import DOA.*;
import spark.*;
import service.UserService;
import service.GameService;

public class Server {
    private UserService userService;
    private GameService gameService;


    private void createServices(){
        UsrDOA userDoa = new MemoryUserDOA();
        AuthDOA authDao = new MemoryAuthDAO();
        GameDAO gameDao = new MemoryGameDAO();

        userService =  new UserService(userDoa, authDao, gameDao);
        gameService = new GameService(authDao, gameDao);

    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        createServices();
        RegisterRouts();
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void RegisterRouts(){
        UserHandler userHandler = new UserHandler(userService);
        Spark.post("/session", userHandler::Login);
        Spark.post("/user", userHandler::Register);
        Spark.delete("/db", this::Clear);
        Spark.delete("/session", userHandler::Logout);

        GameHandler gameHandler = new GameHandler(gameService);
        Spark.post("/game", gameHandler::CreateGame);
        Spark.get("/game", gameHandler::ListGames);
        Spark.put("/game", gameHandler::JoinGame);
    }

    private Object Clear(Request request, Response response) {
        userService.clear();
//        need to add gameService.clear();

        return "{}";
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


}
