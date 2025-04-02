package websocket.messages;

public class NotificationMessage extends ServerMessage{

    private String message;

    public NotificationMessage() {
        this("");
    }

    public NotificationMessage(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
