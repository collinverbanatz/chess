package websocket.commands;

public class LeaveGameCommand extends UserGameCommand{


    public LeaveGameCommand(String authToken, Integer gameID) {
        super(CommandType.LEAVE, authToken, gameID);
    }

    public LeaveGameCommand(CommandType commandType, String authToken, Integer gameID) {
        super(commandType, authToken, gameID);
    }
}
