package DOA;

import Models.GameData;
import Service.GameService;
import chess.ChessGame;

import java.util.ArrayList;
import java.util.HashMap;

public interface GameDAO {
    GameService.CreateResult createGame(GameData gameData);

    ArrayList<GameData> getListGames();

    GameData getGameByID(int gameId);

    void joinGame(int gameId, String wantedColor);

    void updateGameData(GameData gameData);

    void clear();
}
