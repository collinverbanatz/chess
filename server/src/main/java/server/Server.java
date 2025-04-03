package server;

import dao.*;
import dataaccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import spark.*;
import service.UserService;
import service.GameService;
import server.websockett.WebSocketHandler;


import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Spark;

public class Server {
    private UserService userService;
    private GameService gameService;
    private WebSocketHandler webSocketHandler;


    private void createServices() throws DataAccessException {
        Usrdao userDoa = new SQLUserdao();
        Authdao authdao = new SQLAuthdao();
        Gamedao gameDao = new SQLGamedao();

        userService =  new UserService(userDoa, authdao, gameDao);
        gameService = new GameService(authdao, gameDao);
        webSocketHandler = new WebSocketHandler(gameService);
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // register your endpoints and handle exceptions here.
        try {
            createServices();
        }
        catch (DataAccessException e){
            return 500;
        }
        registerRouts();

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void registerRouts(){
        Spark.webSocket("/ws", webSocketHandler);


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
