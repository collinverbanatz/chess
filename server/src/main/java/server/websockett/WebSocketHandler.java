package server.websockett;

import chess.ChessGame;
import chess.InvalidMoveException;
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
import websocket.commands.MakeMoveGameCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.Objects;
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
//        session.getRemote().sendString(msg);

        UserGameCommand message = gson.fromJson(msg, UserGameCommand.class);
        switch (message.getCommandType()){
            case CONNECT -> connection(session, message);
            case LEAVE -> leave(session, message);
            case RESIGN -> resign(session, message);
            case MAKE_MOVE -> makeMove(session, msg);
        }
    }

    private void makeMove(Session session, String msg) {
        MakeMoveGameCommand message  = gson.fromJson(msg, MakeMoveGameCommand.class);
        try {
            AuthData authData = authdao.getAuthDataByToken(message.getAuthToken());
            if (authData == null) {
                connections.sendMessage(session, new ErrorMessage("Invalid auth"));
                return;
            }
            var isActive = gamedao.isGameActive(message.getGameID());
            if (!isActive) {
                connections.sendMessage(authData.getUsername(), new ErrorMessage("game already over."));
                return;
            }
            GameData data = gamedao.getGameByID(message.getGameID());
            ChessGame chessGame = data.getGame();
            String blackUserName = data.getBlackUsername();
            String whiteUserName = data.getWhiteUsername();
            if (
                (chessGame.getTeamTurn() == ChessGame.TeamColor.WHITE && !Objects.equals(authData.getUsername(), data.getWhiteUsername()))
                || (chessGame.getTeamTurn() == ChessGame.TeamColor.BLACK && !Objects.equals(authData.getUsername(), data.getBlackUsername()))
            ) {
                connections.sendMessage(authData.getUsername(), new ErrorMessage("not your turn"));
                return;
            }

            try {
                chessGame.makeMove(message.getMove());
                gamedao.updateGameData(data);
            } catch (InvalidMoveException e) {
                connections.sendMessage(authData.getUsername(), new ErrorMessage(e.getMessage()));
                return;
            }
            connections.broadcast(authData.getUsername(), message.getGameID(), new NotificationMessage(authData.getUsername() + " moved"));
            LoadGameMessage loadGameMessage = new LoadGameMessage(data.getGame(), false);
            connections.broadcast(blackUserName, message.getGameID(), loadGameMessage);
            connections.sendMessage(blackUserName, new LoadGameMessage(data.getGame(), true));

            if(chessGame.isInCheckmate(ChessGame.TeamColor.BLACK)){
                connections.broadcast("", message.getGameID(), new NotificationMessage(blackUserName + " is in checkmate"));
                gamedao.markGameInactive(message.getGameID());
                return;
            }
            if(chessGame.isInCheckmate(ChessGame.TeamColor.WHITE)){
                connections.broadcast("", message.getGameID(), new NotificationMessage(whiteUserName + " is in checkmate"));
                gamedao.markGameInactive(message.getGameID());
                return;
            }
            if(chessGame.isInStalemate(ChessGame.TeamColor.BLACK)){
                connections.broadcast("", message.getGameID(), new NotificationMessage("stalemate"));
                gamedao.markGameInactive(message.getGameID());
                return;
            }
            if(chessGame.isInCheck(ChessGame.TeamColor.BLACK)){
                connections.broadcast("", message.getGameID(), new NotificationMessage(blackUserName + " is in check"));
                return;
            }
            if(chessGame.isInCheck(ChessGame.TeamColor.WHITE)){
                connections.broadcast("", message.getGameID(), new NotificationMessage(whiteUserName + " is in check"));
            }


        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            GameData gameData = gamedao.getGameByID(message.getGameID());
            if (!Objects.equals(authdata.getUsername(), gameData.whiteUsername) && !Objects.equals(authdata.getUsername(), gameData.blackUsername)) {
                connections.sendMessage(authdata.getUsername(), new ErrorMessage("observers cannot resign"));
                return;
            }
            connections.broadcast(null, message.getGameID(), new NotificationMessage("\n" + authdata.getUsername() + " resigned"));
            gamedao.markGameInactive(message.getGameID());

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
            if (authdata == null) {
                connections.sendMessage(session, new ErrorMessage("Invalid auth"));
                return;
            }
            connections.add(authdata.username, message.getGameID(), session);
            GameData gameData = gamedao.getGameByID(message.getGameID());
            if (gameData == null) {
                connections.sendMessage(authdata.getUsername(), new ErrorMessage("No game with that ID"));
                return;
            }
            connections.broadcast(authdata.getUsername(), message.getGameID(), new NotificationMessage("\n" + authdata.getUsername() + " joined the game"));
            boolean isBlack = authdata.getUsername().equals(gameData.blackUsername);
//            boolean isGameActive = gamedao.isGameActive(gameData.getGameID());
            LoadGameMessage loadGameMessage = new LoadGameMessage(gameData.getGame(), isBlack);
            connections.sendMessage(authdata.getUsername(), loadGameMessage);
        } catch (DataAccessException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}