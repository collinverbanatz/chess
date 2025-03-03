package server;

import DOA.*;
import spark.*;
import service.UserService;
import service.GameService;

public class Server {
    private UserService userService;
    private GameService gameService;


    private void createServices(){
        UsrDAO userDoa = new MemoryUserDAO();
        AuthDAO authDao = new MemoryAuthDAO();
        GameDAO gameDao = new MemoryGameDAO();

        userService =  new UserService(userDoa, authDao, gameDao);
        gameService = new GameService(authDao, gameDao);

    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // register your endpoints and handle exceptions here.
        createServices();
        registerRouts();
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void registerRouts(){
        UserHandler userHandler = new UserHandler(userService);
        Spark.post("/session", userHandler::login);
        Spark.post("/user", userHandler::register);
        Spark.delete("/db", this::clear);
        Spark.delete("/session", userHandler::logout);

        GameHandler gameHandler = new GameHandler(gameService);
        Spark.post("/game", gameHandler::createGame);
        Spark.get("/game", gameHandler::listGames);
        Spark.put("/game", gameHandler::joinGame);
    }

    private Object clear(Request request, Response response) {
        userService.clear();
//        need to add gameService.clear();

        return "{}";
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


}
