package server;

import spark.*;
import Service.UserService;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        RegisterRouts();
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void RegisterRouts(){
        Spark.post("/session", UserHandler::Login);
        Spark.post("/user", UserHandler::Register);
        Spark.delete("db", this::Clear);
    }

    private final UserService userService = new UserService();

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
