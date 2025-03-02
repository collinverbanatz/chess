package server;

import Service.GameService;
import Service.UserService;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;

import java.util.ArrayList;

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


    public Object ListGames(Request req, Response response) throws DataAccessException {
        ArrayList data;

        String authToken = req.headers("authorization");
        try {
            data = gameService.ListGames(authToken);
        }
        catch (DataAccessException e){
            response.status(401);
            return("{ \"message\": \"Error: unauthorized\" }");
        }
        response.status(200);
        return gson.toJson(data);
    }

    public Object JoinGame(Request req, Response response) throws DataAccessException {
        GameService.JoinGameRequest gameData = gson.fromJson(req.body(), GameService.JoinGameRequest.class);

        String authToken = req.headers("authorization");

        gameService.JoinGame(gameData, authToken);

        response.status(200);
        return "{}";
    }
}
