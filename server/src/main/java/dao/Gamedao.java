package dao;

import dataaccess.DataAccessException;
import models.GameData;
import models.CreateResult;

import java.util.ArrayList;

public interface Gamedao {
    CreateResult createGame(GameData gameData) throws DataAccessException;

    ArrayList<GameData> getListGames() throws DataAccessException;

    GameData getGameByID(int gameId) throws DataAccessException;

    void updateGameData(GameData gameData) throws DataAccessException;

    boolean isGameActive(int gameId) throws DataAccessException;

    void markGameInactive(int gameId);

    void clear();
}
