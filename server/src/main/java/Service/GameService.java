package Service;

import dataaccess.DataAccessException;
import DOA.AuthDOA;
import DOA.MemoryAuthDAO;
import DOA.MemoryUserDOA;
import DOA.UsrDOA;
import Models.AuthData;
import Models.UserData;
import dataaccess.DataAccessException;

public class GameService {


    public CreateResult createGame(String authToken, CreateRequest game) throws DataAccessException {
        if (!authDao.authTokenExists(authToken)){
            throw new DataAccessException("Invalid authToken");
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
}
