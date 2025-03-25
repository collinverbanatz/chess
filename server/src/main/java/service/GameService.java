package service;

import dao.*;
import models.*;
import chess.ChessGame;
import dataaccess.DataAccessException;

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

        if(!Objects.equals(gameData.getPlayerColor(), "WHITE") && !Objects.equals(gameData.getPlayerColor(), "BLACK")){
            throw new DataAccessException("not a valid color");
        }

        int gameId = gameData.getGameID();

        GameData realGameData = gameDao.getGameByID(gameId);
        if(realGameData == null){
            throw new DataAccessException("No game exists");
        }

        String wantedColor = gameData.getPlayerColor();

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


    @Override
    public String toString() {
        return "GameService{" +
                "authdao=" + authdao +
                ", gameDao=" + gameDao +
                '}';
    }
}