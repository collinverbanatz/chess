package dao;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import models.GameData;
import models.UserData;
import service.GameService;
import service.UserService;

import java.sql.SQLException;
import java.util.ArrayList;

public class SQLGamedao implements Gamedao {

    final Gson gson = new Gson();

    public SQLGamedao() throws DataAccessException {
        DatabaseManager.SQLdaoConst(createStatements);

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  game (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(255) ,
              `blackUsername` varchar(255) ,
              `gameName` varchar(255) NOT NULL,
              `chessGame` TEXT NOT NULL,
              PRIMARY KEY (`gameID`)
            )"""
    };

    @Override
    public GameService.CreateResult createGame(GameData gameData) throws DataAccessException {
        try {
            String statement = "INSERT INTO game (whiteUsername, blackUsername,gameName,chessGame) VALUES (?, ?, ?,?)";
            String gameDataString = gson.toJson(gameData.getGame());
            var gameId = DatabaseManager.executeUpdate(statement, gameData.getWhiteUsername(),
                    gameData.blackUsername, gameData.getGameName(), gameDataString);
            if(gameData.getGameName() == null){
                throw new DataAccessException("couldn't create a new game");
            }
            return new GameService.CreateResult(gameId);
        }
        catch (DataAccessException e){
            throw new DataAccessException("couldn't create a new game", e);
        }
    }

    @Override
    public ArrayList<GameData> getListGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, chessGame  FROM game";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                try (var queryResults = preparedStatement.executeQuery()) {
                    while (queryResults.next()) {
                        var gameID = queryResults.getInt("gameID");
                        var whiteUsername = queryResults.getString("whiteUsername");
                        var blackUsername = queryResults.getString("blackUsername");
                        var gameName = queryResults.getString("gameName");
                        var chessGameString = queryResults.getString("chessGame");
                        var chessGame = gson.fromJson(chessGameString, ChessGame.class);
                        GameData gameDate = new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
                        result.add(gameDate);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("could not get list of games", e);
        }
        return result;
    }

    @Override
    public GameData getGameByID(int gameId) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM game WHERE gameID=?";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(1, gameId);
                try (var queryResults = preparedStatement.executeQuery()){
                    if(!queryResults.next()) {
                        return null;
                    }
                    var gameID = queryResults.getInt("gameID");
                    var whiteUsername = queryResults.getString("whiteUsername");
                    var blackUsername = queryResults.getString("blackUsername");
                    var gameName = queryResults.getString("gameName");
                    var chessGameString = queryResults.getString("chessGame");
                    var chessGame = gson.fromJson(chessGameString, ChessGame.class);
                    GameData gameData = new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
                    return gameData;
                }
            }
        }
        catch (DataAccessException | SQLException e) {
            throw new DataAccessException("DataBase Query failed", e);
        }
    }

    @Override
    public void updateGameData(GameData gameData) throws DataAccessException {
        try {
            String statement = "UPDATE game SET whiteUsername = ?, blackUsername =?, gameName =?, chessGame =? WHERE gameID =?";
            String gameDataString = gson.toJson(gameData.getGame());
            DatabaseManager.executeUpdate(statement, gameData.getWhiteUsername(), gameData.blackUsername,
                    gameData.getGameName(), gameDataString, gameData.getGameID());
        }
        catch (DataAccessException e){
            throw new DataAccessException("couldn't create a new game", e);
        }
    }

    @Override
    public void clear() {
        try {
            DatabaseManager.executeUpdate("TRUNCATE TABLE game");
        }
        catch (DataAccessException e){
            System.err.println("could not clear");
        }
    }
}
