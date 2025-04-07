package websocket.messages;

public class LoadGameMessage extends ServerMessage{

    private String chessBoard;
    private boolean isBlack;

    public LoadGameMessage() {
        this("", false);
    }

    public LoadGameMessage(String chessBoard, boolean isBlack) {
        super(ServerMessageType.LOAD_GAME);
        this.chessBoard = chessBoard;
        this.isBlack = isBlack;
    }

    public String getChessBoard() {
        return chessBoard;
    }

    public void setChessBoard(String chessBoard) {
        this.chessBoard = chessBoard;
    }

    public boolean isBlack() {
        return isBlack;
    }

    public void setBlack(boolean black) {
        isBlack = black;
    }
}
