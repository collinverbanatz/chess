package client;


import com.google.gson.Gson;
import dataaccess.DataAccessException;
import models.CreateRequest;
import models.CreateResult;
import models.ListGameResult;
import models.JoinGameRequest;
import models.RegisterRequest;
import models.RegisterResult;
import models.LoginRequest;
import websocket.commands.ConnectGameCommand;
import websocket.commands.UserGameCommand;


import java.io.IOException;


public class ServerFacade {
//    calls the 7 methods one per end point

    HttpCommunicator clientCommunicator;
    String url = "http://localhost:";
    WebsocketCommunicator ws;
    Gson gson = new Gson();

    public ServerFacade() {
        this(8080);
    }

    public ServerFacade(int port) {
        this.clientCommunicator = new HttpCommunicator();
        this.url = url + port;
        try {
            this.ws = new WebsocketCommunicator();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void joinGame(String authToken, int gameID, String clientColor) throws IOException {
        JoinGameRequest joinGameRequest = new JoinGameRequest(clientColor, gameID);
        clientCommunicator.joinGame(url, joinGameRequest, authToken);
    }

    public ListGameResult listGame(String authToken) throws IOException {
        return clientCommunicator.listGame(url, authToken);
    }

    public RegisterResult register(String userName, String password, String email) throws IOException {
        RegisterRequest request = new RegisterRequest(userName, password, email);
        return clientCommunicator.register(url, request);
    }

    public RegisterResult login(String userName, String password) throws IOException {
        LoginRequest request = new LoginRequest(userName, password);
        return clientCommunicator.login(url, request);
    }

    public CreateResult createGame(String authToken, String gameName) throws IOException {
        CreateRequest createRequest = new CreateRequest(gameName);
        return clientCommunicator.createGame(url, createRequest, authToken);
    }

    public void logout(String authToken) throws IOException {
        clientCommunicator.logout(url,authToken);
    }

    public void clear() throws IOException {
        String authToken = "nothing";
        clientCommunicator.clear(url, authToken);
    }

    public void leave(UserGameCommand.CommandType commandType, String authToken, int gameID) throws DataAccessException {
        UserGameCommand newCommand = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
//        call WebsocketCommunicator to send messages to client

        clientCommunicator.leave(authToken,gameID);
    }

    public void connect(String authToken, int gameID) throws IOException {
        ConnectGameCommand connectGameCommand = new ConnectGameCommand(authToken, gameID);
        String message = gson.toJson(connectGameCommand);
        ws.send(message);
    }
}
