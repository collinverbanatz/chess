package DOA;

import Models.GameData;
import Models.UserData;
import Service.GameService;
import chess.ChessGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryGameDAO implements GameDAO{

    private Map<Integer, GameData> AllGameData = new HashMap<>();


    @Override
    public GameService.CreateResult createGame(GameData gameData) {
        AllGameData.put(gameData.getGameID(), gameData);

        return new GameService.CreateResult(gameData.gameID);
    }

    @Override
    public ArrayList<GameData> getListGames() {
        return new ArrayList<>(AllGameData.values());
    }
}
