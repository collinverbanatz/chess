package dao;

import models.GameData;
import service.GameService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Memorygamedao implements Gamedao {

    private static Map<Integer, GameData> allGameData = new HashMap<>();


    @Override
    public GameService.CreateResult createGame(GameData gameData) {
        allGameData.put(gameData.getGameID(), gameData);

        return new GameService.CreateResult(gameData.gameID);
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
    public void joinGame(int gameId, String wantedColor) {
        allGameData.get(gameId);

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
