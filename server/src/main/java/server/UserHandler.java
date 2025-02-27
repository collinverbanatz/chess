package server;

import Models.AuthData;
import Models.UserData;
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;
import Service.UserService;

public class UserHandler {
    static final Gson gson = new Gson();
    static final UserService userService = new UserService();

    // implements loging in the user give a userName and password
    public static Object Login(Request req, Response response){
        UserService.LoginRequest user = gson.fromJson(req.body(), UserService.LoginRequest.class);
        UserService.LoginResult data = null;

        try {
            data = userService.login(user);
        }
        catch (DataAccessException | IllegalStateException e) {
            response.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
        }

        response.status(200);
        return gson.toJson(data);
    }




}
