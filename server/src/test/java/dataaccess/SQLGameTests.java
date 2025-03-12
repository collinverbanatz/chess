package dataaccess;

import chess.ChessGame;
import dao.*;
import models.GameData;
import models.UserData;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SQLGameTests {

    static SQLGamedao gamedao;

    @BeforeAll
    static void makeDataBase() throws DataAccessException {
//   creates one database that will be used for every test
        gamedao = new SQLGamedao();
    }

    @BeforeEach
    void setup() {
        gamedao.clear();
    }

    @Test
    void positiveCreateGame() throws DataAccessException {
        GameData game = new GameData(0, null, null, "Test Game", new ChessGame());

        var result = gamedao.createGame(game);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getGameID() > 0);
    }

    @Test
    void negativeCreateGame(){
        GameData game = new GameData(0, null, null,  null, null);

        assertThrows(DataAccessException.class, () -> gamedao.createGame(game));
    }

    @Test
    void positiveGetListGame() throws DataAccessException {
        GameData game = new GameData(0, null, null, "Test Game", new ChessGame());
        GameData game2 = new GameData(1, null, null, "Test Game2", new ChessGame());

        gamedao.createGame(game);
        gamedao.createGame(game2);
        ArrayList<GameData> games = gamedao.getListGames();
        Assertions.assertEquals(2, games.size());
    }

    @Test
    void negativeGestListGame() throws DataAccessException {
        GameData game = new GameData(0, null, null, "Test Game", new ChessGame());
        GameData game2 = new GameData(1, null, null, "Test Game2", new ChessGame());

        gamedao.createGame(game);
        gamedao.createGame(game2);
        ArrayList<GameData> games = gamedao.getListGames();
        Assertions.assertFalse(1 == games.size());
    }

    @Test
    void positiveGetGameById() throws DataAccessException {
        GameData game = new GameData(0, null, null, "Test Game", new ChessGame());

        var result = gamedao.createGame(game);
        int gameID = result.getGameID();
        GameData chessGame = gamedao.getGameByID(gameID);
        Assertions.assertNotNull(chessGame);
    }


    @Test
    void negativeGetGameById() throws DataAccessException {
        GameData game = new GameData(0, null, null, "Test Game", new ChessGame());

        var result = gamedao.createGame(game);
        int fakeID = 12;
        GameData chessGame = gamedao.getGameByID(fakeID);
        Assertions.assertNull(chessGame);
    }

    @Test
    void positiveUpdateGameData() throws DataAccessException {
        GameData game = new GameData(0, null, null, "Test Game", new ChessGame());

        var result = gamedao.createGame(game);
        int gameID = result.getGameID();
        GameData newGameData = new GameData(gameID, "collin", "notCollin", "newGame", new ChessGame());

        gamedao.updateGameData(newGameData);
        GameData retrivedGameData = gamedao.getGameByID(gameID);
        Assertions.assertNotNull(retrivedGameData);
        Assertions.assertEquals("collin", newGameData.whiteUsername);
    }

    @Test
    void negativeUpdateGameDate() throws DataAccessException {
        GameData game = new GameData(25, null, null, "Test Game", new ChessGame());
        gamedao.createGame(game);
        Assertions.assertNull(gamedao.getGameByID(25));
//        Assertions.assertThrows(DataAccessException.class, () -> gamedao.updateGameData(game2));
    }

    @Test
    void clear() throws DataAccessException {
        GameData game = new GameData(25, null, null, "Test Game", new ChessGame());
        gamedao.createGame(game);
        int gameID = game.getGameID();

        gamedao.clear();
        GameData gameData = gamedao.getGameByID(gameID);
        Assertions.assertNull(gameData);
    }
}
