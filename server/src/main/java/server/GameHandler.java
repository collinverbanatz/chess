package server;

import Service.GameService;
import Service.UserService;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;

public class GameHandler {
    Gson gson = new Gson();
    final GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Object CreateGame(Request req, Response response) throws DataAccessException {
        GameService.CreateRequest gameData = gson.fromJson(req.body(), GameService.CreateRequest.class);
        GameService.CreateResult data;

        String authToken = req.headers("authorization");

        try {
            data = gameService.createGame(authToken, gameData);
        }
        catch (DataAccessException e){
            response.status(401);
            return ("{ \"message\": \"Error: unauthorized\" }");
        }

        response.status(200);
        return gson.toJson(data);
    }


    public Object ListGames(Request req, Response response) {
        GameService.ListGameResult data;

        String authToken = req.headers("authorization");

        data = gameService.ListGames(authToken);


        response.status(200);
        return gson.toJson(data);
    }
}
