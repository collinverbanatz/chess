package server;

import dataaccess.DataAccessException;
import models.LoginRequest;
import models.RegisterRequest;
import models.RegisterResult;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;
import service.UserService;

public class UserHandler {
     final Gson gson = new Gson();
     final UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    // implements loging in the user give a userName and password
    public Object login(Request req, Response response){
        LoginRequest user = gson.fromJson(req.body(), LoginRequest.class);
        RegisterResult data;

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



    public  Object register(Request req, Response response){
        RegisterRequest user = gson.fromJson(req.body(), RegisterRequest.class);
        RegisterResult data;

        if(user.getPassword() == null || user.getUsername() == null || user.getEmail() == null ){
            response.status(400);
            return ("{ \"message\": \"Error: bad request\" }");
        }

        try {
            data = userService.register(user);
        }
        catch (DataAccessException e) {
            response.status(403);
            return "{ \"message\": \"Error: already taken\" }";
        }

        response.status(200);
        return gson.toJson(data);
    }

    public  Object logout(Request req, Response response) throws DataAccessException {
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
