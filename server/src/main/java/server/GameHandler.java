package server;

import service.GameService;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class GameHandler {
    Gson gson = new Gson();
    final GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Object createGame(Request req, Response response) throws DataAccessException {
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


    public Object listGames(Request req, Response response) throws DataAccessException {
        GameService.ListGameResult data;

        String authToken = req.headers("authorization");
        try {
            data = gameService.listGames(authToken);
            System.out.println("Raw ListGameResult object: " + data);

            // Convert to JSON
            String jsonResponse = gson.toJson(data);

            // Debugging: Print the JSON string
            System.out.println("Serialized JSON response: " + jsonResponse);

            response.status(200);
            return jsonResponse;
        }
        catch (DataAccessException e){
            response.status(401);
            return("{ \"message\": \"Error: unauthorized\" }");
        }
    }


    public Object joinGame(Request req, Response response) throws DataAccessException {
        GameService.JoinGameRequest gameData = gson.fromJson(req.body(), GameService.JoinGameRequest.class);

        String authToken = req.headers("authorization");
        if(authToken == null){
            response.status(401);
            return ("{ \"message\": \"Error: unauthorized\" }");
        }

        try {
            gameService.joinGame(gameData, authToken);
        }
        catch (DataAccessException e){
            String message = e.getMessage();
            if(Objects.equals(message, "Invalid authToken")){
                response.status(401);
                return ("{ \"message\": \"Error: unauthorized\" }");
            }
            if(Objects.equals(message, "No game exists")){
                response.status(400);
                return(" { \"message\": \"Error: bad request\" }");
            }
            if(Objects.equals(message, "already taken")){
                response.status(403);
                return ("{ \"message\": \"Error: already taken\" }");
            }
            if(Objects.equals(message, "not a valid color")){
                response.status(400);
                return(" { \"message\": \"Error: bad request\" }");
            }
        }
        response.status(200);
        return "{}";
    }
}
