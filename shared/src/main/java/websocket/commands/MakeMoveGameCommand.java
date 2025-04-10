package websocket.commands;

import chess.ChessGame;
import chess.ChessMove;

public class MakeMoveGameCommand extends UserGameCommand{


    public MakeMoveGameCommand(String authToken, Integer gameID, ChessMove move) {
        this(CommandType.MAKE_MOVE, authToken, gameID, move);
    }

    public MakeMoveGameCommand(CommandType commandType, String authToken, Integer gameID, ChessMove move) {
        super(commandType, authToken, gameID);
        this.move = move;
    }
    private ChessMove move;

    public ChessMove getMove() {
        return move;
    }

    public void setMove(ChessMove move) {
        this.move = move;
    }
}

