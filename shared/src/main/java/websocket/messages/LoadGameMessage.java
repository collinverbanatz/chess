package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage{

    private ChessGame game;
    private boolean isBlack;
//    private boolean isGameActive;

    public LoadGameMessage() {
        this(null, false);
    }

    public LoadGameMessage(ChessGame game, boolean isBlack) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
        this.isBlack = isBlack;
//        this.isGameActive =  isGameActive;
    }

    public ChessGame getGame() {
        return game;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }

    public boolean isBlack() {
        return isBlack;
    }

    public void setBlack(boolean black) {
        isBlack = black;
    }

}
