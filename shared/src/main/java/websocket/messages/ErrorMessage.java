package websocket.messages;

public class ErrorMessage extends ServerMessage{

    private String message;

    public ErrorMessage() {
        this("");
    }

    public ErrorMessage(String message) {
        super(ServerMessageType.ERROR);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
