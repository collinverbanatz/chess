package server;

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
        UserService.LoginResult data;

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



    public static Object Register(Request req, Response response){
        UserService.RegisterRequest user = gson.fromJson(req.body(), UserService.RegisterRequest.class);
        UserService.RegisterResult data;


        try {
            data = userService.register(user);
        }
        catch (DataAccessException | IllegalStateException e) {
            response.status(403);
            return "{ \"message\": \"Error: already taken\" }";
        }

        response.status(200);
        return gson.toJson(data);
    }

    public static Object Logout(Request req, Response response) throws DataAccessException {
//        UserService.LogoutRequest user = gson.fromJson(req.body(), UserService.LogoutRequest.class);
//        UserService.LogoutResult data;
        String authToken = req.headers("authorization");

        if(authToken == null || authToken.isEmpty()){
            response.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
        }

        try {
            userService.logout(authToken);
        }
        catch(DataAccessException e){
            response.status(500);
            return "{ \"message\": \"Error: Invalid authToken\" }";
        }

        response.status(200);
        return "{}";
    }




}
