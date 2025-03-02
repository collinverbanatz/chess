package Service;

import DOA.*;
import Models.GameData;
import chess.ChessGame;
import dataaccess.DataAccessException;

import java.util.ArrayList;
import java.util.HashSet;

public class GameService {

    AuthDOA authDao;
    GameDAO gameDao;

    public GameService(AuthDOA authDao, GameDAO gameDao) {
        this.authDao = authDao;
        this.gameDao = gameDao;
    }

    int gameID = 0;


    public CreateResult createGame(String authToken, CreateRequest game) throws DataAccessException {

        if(authToken == null){
            throw new DataAccessException("name can't be null");
        }

//        check to see if authToken exists
        if (!authDao.authTokenExists(authToken)){
            throw new DataAccessException("Invalid authToken");
        }

        gameID = gameID +1;
        ChessGame chessGame = new ChessGame();
        return gameDao.createGame(new GameData(gameID, null, null, game.getGameName(), chessGame));
    }



    public ArrayList ListGames(String authToken) throws DataAccessException {
        if (!authDao.authTokenExists(authToken)){
            throw new DataAccessException("Invalid authToken");
        }
        return gameDao.getListGames();
    }


    public void JoinGame(JoinGameRequest gameData, String authToken) throws DataAccessException {
        if (!authDao.authTokenExists(authToken)){
            throw new DataAccessException("Invalid authToken");
        }
        int gameId = gameData.getGameID();

        if(!gameDao.getGameID(gameId)){
            throw new DataAccessException("InvalidGameId");
        }

        GameData gameData2 = gameDao.getGame(gameId);



//       GameData gameData1 = gameDao.getGameData();
//       int realGameId = gameData1.gameID;
//        if(gameId != realGameId){
//            throw new DataAccessException("InvalidGameId");
//        }


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



    public static class ListGameResult{
        private HashSet gameLlist;
    }



    public static class JoinGameRequest{
        private String playerColor;
        private int gameID;

        public JoinGameRequest(String playerColor, int gameID) {
            this.playerColor = playerColor;
            this.gameID = gameID;
        }

        public String getPlayerColor() {
            return playerColor;
        }

        public void setPlayerColor(String playerColor) {
            this.playerColor = playerColor;
        }

        public int getGameID() {
            return gameID;
        }

        public void setGameID(int gameID) {
            this.gameID = gameID;
        }
    }

}