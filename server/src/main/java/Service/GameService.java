package Service;

import DOA.*;
import Models.GameData;
import chess.ChessGame;
import dataaccess.DataAccessException;

public class GameService {
//    UsrDOA userDao = new MemoryUserDOA();
//    AuthDOA authDao = new MemoryAuthDAO();
    GameDAO gameDao = new MemoryGameDAO();
    int gameID = 0;


    public CreateResult createGame(String authToken, CreateRequest game) throws DataAccessException {

//        if(authToken == null){
//            throw new DataAccessException("name can't be null");
//        }

//        check to see if authToken exists
        if (!UserService.authDao.authTokenExists(authToken)){
            throw new DataAccessException("Invalid authToken");
        }


        gameID = gameID +1;

        ChessGame chessGame = new ChessGame();

        return gameDao.createGame(new GameData(gameID, null, null, game.getGameName(), chessGame));


//        psudo code
//        create game id
//        create chess board
//        daoGame to put game id and board into a map
//        return CreateResult data (will include gameID

    }

    public static class CreateRequest{
            private String gameName;

            public CreateRequest(String gameName) {
                this.gameName = gameName;
            }

            public String getGameName() {
                return gameName;
            }

            public void setGameName(String gameName) {
                this.gameName = gameName;
            }
        }



        public static class CreateResult{
            private int gameID;

            public CreateResult(int gameID) {
                this.gameID = gameID;
            }

            public int getGameID() {
                return gameID;
            }

            public void setGameID(int gameID) {
                this.gameID = gameID;
            }
        }
}
