package net;

import models.AuthData;
import models.GameData;
import service.GameService;
import service.UserService;
import service.UserService.*;
import com.google.gson.Gson;
import models.AuthData;
import models.GameData;
import service.UserService.RegisterRequest;
import service.UserService.RegisterResult;
import service.UserService.LoginRequest;
import service.GameService.CreateResult;
import dataaccess.DataAccessException;

import java.io.IOException;


public class ServerFacade {
//    calls the 7 methods one per end point

    ClientCommunicator clientCommunicator;
    String url = "http://localhost:8080";

    public ServerFacade() {
        this.clientCommunicator = new ClientCommunicator();
    }

    public GameService.ListGameResult listGame(String authToken) throws IOException {
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

    public GameService.CreateResult createGame(String authToken, String gameName) throws IOException {
        GameService.CreateRequest createRequest = new GameService.CreateRequest(gameName);
        return clientCommunicator.createGame(url, createRequest, authToken);
    }
}
