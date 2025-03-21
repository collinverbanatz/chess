package models;

import chess.ChessGame;

public class GameData {
    public int gameID;;
    public String whiteUsername;
    public String blackUsername;
    public String gameName;
    public transient ChessGame game;

    public GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
    }

    public int getGameID() {
        return gameID;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public ChessGame getGame() {
        return game;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }

    @Override
    public String toString() {
        return "GameData{id=" + gameID +
                ", white='" + whiteUsername + '\'' +
                ", black='" + blackUsername + '\'' +
                ", name='" + gameName + '\'' +
                "}";
    }
}
