package dao;

import models.GameData;
import models.CreateResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Memorygamedao implements Gamedao {

    private static Map<Integer, GameData> allGameData = new HashMap<>();
    int gameID = 0;


    @Override
    public CreateResult createGame(GameData gameData) {
        gameID = gameID + 1;
        gameData.setGameID(gameID);
        allGameData.put(gameID, gameData);

        return new CreateResult(gameData.gameID);
    }

    @Override
    public ArrayList<GameData> getListGames() {
        return new ArrayList<>(allGameData.values());
    }

    @Override
    public GameData getGameByID(int gameId) {
        return allGameData.get(gameId);
    }

    @Override
    public void updateGameData(GameData gameData) {
        allGameData.put(gameData.getGameID(), gameData);
    }

    @Override
    public void clear() {
        allGameData.clear();
    }
}
