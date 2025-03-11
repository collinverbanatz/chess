package dao;

import dataaccess.DataAccessException;
import models.GameData;
import service.GameService;

import java.sql.SQLException;
import java.util.ArrayList;

public class SQLGamedao implements Gamedao {

    public SQLGamedao() throws DataAccessException {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try (var conn = DatabaseManager.getConnection()) {
            for (var state : createStatements) {
                try (var preparedStatement = conn.prepareStatement(state)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("could not connect to Database");
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  game (
              `gameID` varchar(255) NOT NULL,
              `whiteUsername` varchar(255) NOT NULL,
              `blackUsername` varchar(255) NOT NULL,
              `gameName` varchar(255) NOT NULL,
              `chessGame` TEXT,
              PRIMARY KEY (`gameID`)
            )"""
    };

    @Override
    public GameService.CreateResult createGame(GameData gameData) {
        return null;
    }

    @Override
    public ArrayList<GameData> getListGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, chessGame, json FROM users";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                try (var executeQuery = preparedStatement.executeQuery()) {
                    while (executeQuery.next()) {

                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("could not get list of games");
        }
        return result;
    }

    @Override
    public GameData getGameByID(int gameId) {
        return null;
    }

    @Override
    public void joinGame(int gameId, String wantedColor) {

    }

    @Override
    public void updateGameData(GameData gameData) {

    }

    @Override
    public void clear() {

    }
}
