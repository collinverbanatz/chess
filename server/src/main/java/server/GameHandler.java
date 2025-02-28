package server;

import Service.GameService;
import Service.UserService;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

public class GameHandler {
    static final Gson gson = new Gson();
    static final GameService gameService = new GameService();

    public static Object CreateGame(Request req, Response response) {
        GameService.CreateRequest gameData = gson.fromJson(req.body(), GameService.CreateRequest.class);
        GameService.CreateResult data;

        String authToken = req.headers("authorization");
        data = gameService.createGame(authToken, gameData);


        response.status(200);
        return gson.toJson(data);
    }


}
