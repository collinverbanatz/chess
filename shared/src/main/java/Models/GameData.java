package Models;

import chess.ChessGame;

public class GameData {
    public int gameID;;
    public String whiteUserName;
    public String blackUserName;
    public String gameName;
    public ChessGame game;

    public GameData(int gameID, String whiteUserName, String blackUserName, String gameName, ChessGame game) {
        this.gameID = gameID;
        this.whiteUserName = whiteUserName;
        this.blackUserName = blackUserName;
        this.gameName = gameName;
        this.game = game;
    }

    public int getGameID() {
        return gameID;
    }

    public String getWhiteUserName() {
        return whiteUserName;
    }

    public String getBlackUserName() {
        return blackUserName;
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

    public void setWhiteUserName(String whiteUserName) {
        this.whiteUserName = whiteUserName;
    }

    public void setBlackUserName(String blackUserName) {
        this.blackUserName = blackUserName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }
}
