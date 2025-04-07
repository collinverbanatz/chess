package client;

import chess.ChessBoard;
import com.google.gson.Gson;
import ui.Client;
import ui.DrawChessBoard;
import websocket.commands.LeaveGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

public class WebsocketCommunicator extends Endpoint {

    public Session session;
    Gson gson = new Gson();


    public WebsocketCommunicator() throws Exception {
        URI uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
                switch (serverMessage.getServerMessageType()){
                    case NOTIFICATION -> handleNotification(gson.fromJson(message, NotificationMessage.class));
                    case LOAD_GAME -> handleLoadGame(gson.fromJson(message, LoadGameMessage.class));
                }
            }
        });
    }

    private void handleLoadGame(LoadGameMessage loadGameMessage) {
        System.out.println(loadGameMessage.getChessBoard());
        ChessBoard chessBoard = gson.fromJson(loadGameMessage.getChessBoard(), ChessBoard.class);
        boolean isWhite = !loadGameMessage.isBlack();
        Client.lastChessBoard = chessBoard;
        Client.lastWasWhite = isWhite;
        Client.printBoardAndHelp();
    }

    private void handleNotification(NotificationMessage msg) {
        System.out.println(msg.getMessage());
    }

    public void send(String msg) throws IOException {
        this.session.getBasicRemote().sendText(msg);
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
