package server.websockett;

import com.google.gson.Gson;
//import dataaccess.DataAccess;
//import exception.ResponseException;
import dao.Authdao;
import dao.SQLAuthdao;
import dataaccess.DataAccessException;
import models.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import spark.Spark;
//import webSocketMessages.Action;
//import webSocketMessages.Notification;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;


@WebSocket
public class WebSocketHandler {
    Gson gson = new Gson();
    GameService gameService;
    Authdao authdao;
    {
        try {
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
            connections.broadcast(authdata.getUsername(), message.getGameID(), new NotificationMessage("\n" + authdata.getUsername() + " resigned"));

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
        } catch (DataAccessException | IOException e) {
            throw new RuntimeException(e);
        }
    }

//    @OnWebSocketMessage
//    public void onMessage(Session session, String msg) {
//        try {
//            UserGameCommand command = Serializer.fromJson(message, UserGameCommand.class);
//
//            // Throws a custom UnauthorizedException. Yours may work differently.
//            String username = getUsername(command.getAuthToken());
//
//            saveSession(command.getGameID(), session);
//
//            switch (command.getCommandType()) {
//                case CONNECT -> connection(session, username, (ConnectCommand) command);
//                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
//                case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
//                case RESIGN -> resign(session, username, (ResignCommand) command);
//            }
//        } catch (UnauthorizedException ex) {
//            // Serializes and sends the error message
//            sendMessage(session.getRemote(), new ErrorMessage("Error: unauthorized"));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage()));
//        }
//    }
//
//
//    private void connection(Session session, String username, ConnectCommand command) {
//
//    }
//
//    private void makeMove(Session session, String username, MakeMoveCommand command) {
//    }
//
//    private void resign(Session session, String username, ResignCommand command) {
//    }
//
//    private void leaveGame(Session session, String username, LeaveGameCommand command) {
//
//    }
}