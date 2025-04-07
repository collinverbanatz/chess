package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage{

    private ChessGame chessGame;
    private boolean isBlack;
//    private boolean isGameActive;

    public LoadGameMessage() {
        this(null, false);
    }

    public LoadGameMessage(ChessGame chessGame, boolean isBlack) {
        super(ServerMessageType.LOAD_GAME);
        this.chessGame = chessGame;
        this.isBlack = isBlack;
//        this.isGameActive =  isGameActive;
    }

    public ChessGame getChessGame() {
        return chessGame;
    }

    public void setChessGame(ChessGame chessGame) {
        this.chessGame = chessGame;
    }

    public boolean isBlack() {
        return isBlack;
    }

    public void setBlack(boolean black) {
        isBlack = black;
    }

//    public boolean isGameActive() {
//        return isGameActive;
//    }
//
//    public void setGameActive(boolean gameActive) {
//        isGameActive = gameActive;
//    }
}
