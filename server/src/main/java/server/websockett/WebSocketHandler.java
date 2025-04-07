package server.websockett;

import chess.ChessGame;
import com.google.gson.Gson;
//import dataaccess.DataAccess;
//import exception.ResponseException;
import dao.Authdao;
import dao.Gamedao;
import dao.SQLAuthdao;
import dao.SQLGamedao;
import dataaccess.DataAccessException;
import models.AuthData;
import models.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import spark.Spark;
//import webSocketMessages.Action;
//import webSocketMessages.Notification;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;


@WebSocket
public class WebSocketHandler {
    Gson gson = new Gson();
    GameService gameService;
    Gamedao gamedao;
    Authdao authdao;
    {
        try {
            gamedao = new SQLGamedao();
            authdao = new SQLAuthdao();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public WebSocketHandler(GameService gameService) {
        this.gameService = gameService;
    }

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws IOException {
//        System.out.printf("Received: %s\n", msg);
        session.getRemote().sendString("WebSocket response: " + msg);

        UserGameCommand message =gson.fromJson(msg, UserGameCommand.class);
        switch (message.getCommandType()){
            case CONNECT -> connection(session, message);
            case LEAVE -> leave(session, message);
            case RESIGN -> resign(session, message);
        }
    }

    private void resign(Session session, UserGameCommand message) {
        try {
            AuthData authdata = authdao.getAuthDataByToken(message.getAuthToken());
            var isActive = gamedao.isGameActive(message.getGameID());
            if (!isActive) {
                connections.sendMessage(authdata.getUsername(), new ErrorMessage("game already over."));
                return;
            }
            connections.broadcast(authdata.getUsername(), message.getGameID(), new NotificationMessage("\n" + authdata.getUsername() + " resigned"));
            gamedao.markGameInactive(message.getGameID());
//            GameData gameData = gamedao.getGameByID(message.getGameID());
//            boolean isBlack = authdata.getUsername().equals(gameData.blackUsername);
//            boolean isGameActive = gamedao.isGameActive(gameData.getGameID());
//            LoadGameMessage loadGameMessage = new LoadGameMessage(gameData.getGame(), isBlack, isGameActive);
//            connections.broadcast("", message.getGameID(), loadGameMessage);
        } catch (DataAccessException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void leave(Session session, UserGameCommand message){
        try {
            AuthData authdata = authdao.getAuthDataByToken(message.getAuthToken());
            connections.remove(authdata.username);
            connections.broadcast(authdata.getUsername(), message.getGameID(), new NotificationMessage("\n" + authdata.getUsername() + " left the game."));
//            System.out.printf("about to leave game as with ID " + authdata.getUsername() + message.getGameID());
            gameService.leave(message.getAuthToken(), message.getGameID());

        } catch (DataAccessException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void connection(Session session, UserGameCommand message) {
        try {
            AuthData authdata = authdao.getAuthDataByToken(message.getAuthToken());
            connections.add(authdata.username, message.getGameID(), session);
            connections.broadcast(authdata.getUsername(), message.getGameID(), new NotificationMessage("\n" + authdata.getUsername() + " joined the game"));
            GameData gameData = gamedao.getGameByID(message.getGameID());
            boolean isBlack = authdata.getUsername().equals(gameData.blackUsername);
//            boolean isGameActive = gamedao.isGameActive(gameData.getGameID());
            LoadGameMessage loadGameMessage = new LoadGameMessage(gameData.getGame(), isBlack);
            connections.sendMessage(authdata.getUsername(), loadGameMessage);
        } catch (DataAccessException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}