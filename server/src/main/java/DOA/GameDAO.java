package DOA;

import Models.GameData;
import Service.GameService;
import chess.ChessGame;

public interface GameDAO {
    GameService.CreateResult createGame(GameData gameData);
}
