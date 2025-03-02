package server;

import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;
import Service.UserService;

public class UserHandler {
     final Gson gson = new Gson();
     final UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    // implements loging in the user give a userName and password
    public Object Login(Request req, Response response){
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



    public  Object Register(Request req, Response response){
        UserService.RegisterRequest user = gson.fromJson(req.body(), UserService.RegisterRequest.class);
        UserService.RegisterResult data;

        if(user.getPassword() == null || user.getUsername() == null || user.getEmail() == null ){
            response.status(400);
            return ("{ \"message\": \"Error: bad request\" }");
        }

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

    public  Object Logout(Request req, Response response) throws DataAccessException {
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
            response.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
        }

        response.status(200);
        return "{}";
    }




}
