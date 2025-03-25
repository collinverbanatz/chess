package models;

import java.util.List;

public class ListGameResult {
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
