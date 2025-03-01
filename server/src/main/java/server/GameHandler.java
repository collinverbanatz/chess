package server;

import Service.GameService;
import Service.UserService;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;

public class GameHandler {
    Gson gson = new Gson();
    GameService gameService;

    public Object CreateGame(Request req, Response response) throws DataAccessException {
        GameService.CreateRequest gameData = gson.fromJson(req.body(), GameService.CreateRequest.class);
        GameService.CreateResult data;

        String authToken = req.headers("authorization");
        data = gameService.createGame(authToken, gameData);


        response.status(200);
        return gson.toJson(data);
    }


}
