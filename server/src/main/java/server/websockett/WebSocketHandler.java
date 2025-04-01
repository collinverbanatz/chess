package server.websockett;

import com.google.gson.Gson;
//import dataaccess.DataAccess;
//import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.io.ConnectionManager;
import spark.Spark;
//import webSocketMessages.Action;
//import webSocketMessages.Notification;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {

//    private final ConnectionManager connections = new ConnectionManager();

    public static void main(String[] args) {
        Spark.port(8080);
        Spark.webSocket("/ws", WebSocketHandler.class);
        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) {
        try {
            UserGameCommand command = Serializer.fromJson(message, UserGameCommand.class);

            // Throws a custom UnauthorizedException. Yours may work differently.
            String username = getUsername(command.getAuthToken());

            saveSession(command.getGameID(), session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, (ConnectCommand) command);
                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
                case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
                case RESIGN -> resign(session, username, (ResignCommand) command);
            }
        } catch (UnauthorizedException ex) {
            // Serializes and sends the error message
            sendMessage(session.getRemote(), new ErrorMessage("Error: unauthorized"));
        } catch (Exception ex) {
            ex.printStackTrace();
            sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage()));
        }
    }

    private void leaveGame(Session session, String username, LeaveGameCommand command) {

    }
}