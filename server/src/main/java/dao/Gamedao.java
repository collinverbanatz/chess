package dao;

import dataaccess.DataAccessException;
import models.GameData;
import service.GameService;

import java.util.ArrayList;

public interface Gamedao {
    GameService.CreateResult createGame(GameData gameData);

    ArrayList<GameData> getListGames() throws DataAccessException;

    GameData getGameByID(int gameId);

    void joinGame(int gameId, String wantedColor);

    void updateGameData(GameData gameData);

    void clear();
}
