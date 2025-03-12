package dao;

import dataaccess.DataAccessException;
import models.GameData;
import service.GameService;

import java.util.ArrayList;

public interface Gamedao {
    GameService.CreateResult createGame(GameData gameData) throws DataAccessException;

    ArrayList<GameData> getListGames() throws DataAccessException;

    GameData getGameByID(int gameId) throws DataAccessException;

    void updateGameData(GameData gameData) throws DataAccessException;

    void clear();
}
