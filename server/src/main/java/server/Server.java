package server;

import spark.*;

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
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
