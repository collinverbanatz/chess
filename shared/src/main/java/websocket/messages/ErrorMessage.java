package websocket.messages;

public class ErrorMessage extends ServerMessage{

    private String errorMessage;

    public ErrorMessage() {
        this("");
    }

    public ErrorMessage(String message) {
        super(ServerMessageType.ERROR);
        this.errorMessage = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
