package server;

import dao.*;
import dataaccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import spark.*;
import service.UserService;
import service.GameService;

import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Spark;

public class Server {
    private UserService userService;
    private GameService gameService;


    private void createServices() throws DataAccessException {
        Usrdao userDoa = new SQLUserdao();
        Authdao authdao = new SQLAuthdao();
        Gamedao gameDao = new SQLGamedao();

        userService =  new UserService(userDoa, authdao, gameDao);
        gameService = new GameService(authdao, gameDao);

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

//    @WebSocket
//    public class WSServer {
//        public static void main(String[] args) {
//            Spark.port(8080);
//            Spark.webSocket("/ws", WSServer.class);
//            Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
//        }
//
//        @OnWebSocketMessage
//        public void onMessage(Session session, String message) throws Exception {
//            System.out.printf("Received: %s", message);
//            session.getRemote().sendString("WebSocket response: " + message);
//        }
//    }

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
