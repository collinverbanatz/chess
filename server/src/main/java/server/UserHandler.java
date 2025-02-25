package server;

import chess.ChessGame;
import org.w3c.dom.UserDataHandler;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;
import Service.UserService;

public class UserHandler {
    static final Gson gson = new Gson();

    // implements loging in the user give a userName and password
    public static Object Login(Request req, Response response){
        userData user = gson.fromJson(req.body(), userData.class);
        AuthData data = UserService.loginUser(user.userName, user.password);

        response.status(200);
        return gson.toJson(data);
    }


    //class to clas out userName and Password out of the object after being converted from gson
    public class userData {
        public String userName;
        public String password;

        public UserData(String userName, String password){
            this.userName = userName;
            this.password = password;
        }

        public String getuserName(){
            return userName;
        }

        public String getPassword(){
            return password;
        }
    }

    // class to get authToken
    public class AuthData{
        public String authToken;

        public AuthData(String authToken){
            this.authToken = authToken;
        }

        public String getAuthToken(){
            return authToken;
        }
    }
}
