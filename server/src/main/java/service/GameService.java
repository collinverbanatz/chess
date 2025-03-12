package service;

import dao.*;
import models.AuthData;
import models.GameData;
import chess.ChessGame;
import dataaccess.DataAccessException;

import java.util.List;
import java.util.Objects;

public class GameService {

    Authdao authdao;
    Gamedao gameDao;

    public GameService(Authdao authdao, Gamedao gameDao) {
        this.authdao = authdao;
        this.gameDao = gameDao;
    }



    public CreateResult createGame(String authToken, CreateRequest game) throws DataAccessException {

        if(authToken == null){
            throw new DataAccessException("name can't be null");
        }

//        check to see if authToken exists
        if (!authdao.authTokenExists(authToken)){
            throw new DataAccessException("Invalid authToken");
        }

        ChessGame chessGame = new ChessGame();
        return gameDao.createGame(new GameData(-1, null, null, game.getGameName(), chessGame));
    }



    public ListGameResult listGames(String authToken) throws DataAccessException {
        if (!authdao.authTokenExists(authToken)){
            throw new DataAccessException("Invalid authToken");
        }
        return new ListGameResult(gameDao.getListGames());
    }


    public void joinGame(JoinGameRequest gameData, String authToken) throws DataAccessException {
        AuthData authData = authdao.getAuthDataByToken(authToken);
        if(authData == null) {
            throw new DataAccessException("Invalid authToken");
        }

        if(!Objects.equals(gameData.playerColor, "WHITE") && !Objects.equals(gameData.playerColor, "BLACK")){
            throw new DataAccessException("not a valid color");
        }

        int gameId = gameData.getGameID();

        GameData realGameData = gameDao.getGameByID(gameId);
        if(realGameData == null){
            throw new DataAccessException("No game exists");
        }

        String wantedColor = gameData.playerColor;

        if(wantedColor.equals("BLACK") && realGameData.blackUsername != null || wantedColor.equals("WHITE") && realGameData.whiteUsername != null){
            throw new DataAccessException("already taken");
        }

        String userName = authData.username;
        if (wantedColor.equals("BLACK")) {
            realGameData.setBlackUsername(userName);
        }
        else{
            realGameData.setWhiteUsername(userName);
        }
        gameDao.updateGameData(realGameData);
    }


    public static class ListGameResult{
        private List<GameData> games;

        public ListGameResult(List<GameData> games) {
            this.games = games;
        }

        public List<GameData> getGames() {
            return games;
        }

        public void setGames(List<GameData> games) {
            this.games = games;
        }
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