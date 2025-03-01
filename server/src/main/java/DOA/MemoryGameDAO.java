package DOA;

import Models.GameData;
import Models.UserData;
import Service.GameService;
import chess.ChessGame;

import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO{

    private Map<Integer, ChessGame> AllGameData = new HashMap<>();


    @Override
    public GameService.CreateResult createGame(GameData gameData) {
        AllGameData.put(gameData.getGameID(), gameData.getGame());


        return new GameService.CreateResult(gameData.gameID);
    }
}
