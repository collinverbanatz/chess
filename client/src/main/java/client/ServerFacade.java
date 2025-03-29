package client;


import models.CreateRequest;
import models.CreateResult;
import models.ListGameResult;
import models.JoinGameRequest;
import models.RegisterRequest;
import models.RegisterResult;
import models.LoginRequest;


import java.io.IOException;


public class ServerFacade {
//    calls the 7 methods one per end point

    HttpCommunicator clientCommunicator;
    String url = "http://localhost:";

    public ServerFacade() {
        this(8080);
    }

    public ServerFacade(int port) {
        this.clientCommunicator = new HttpCommunicator();
        this.url = url + port;
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
}
