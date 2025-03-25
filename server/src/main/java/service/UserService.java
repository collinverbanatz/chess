package service;

import dao.Authdao;
import dao.Gamedao;
import dao.Usrdao;
import models.*;
import dataaccess.DataAccessException;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

public class UserService {


    Usrdao userDao;
    Authdao authdao;
    Gamedao gameDao;

    public  UserService(Usrdao userDoa, Authdao authdao, Gamedao gameDao){
        this.authdao = authdao;
        this.userDao = userDoa;
        this.gameDao = gameDao;
    }


    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        UserData userData = userDao.getUser(registerRequest.getUsername());
        if(userData != null){
            throw new DataAccessException("username all ready exists");
        }
        userDao.putUser(new UserData(registerRequest.getUsername(), hashPassword(registerRequest.getPassword()), registerRequest.getEmail()));
        AuthData authData = createAndSaveAuthToken(registerRequest.getUsername());
        return new RegisterResult(registerRequest.getUsername(), authData.authToken);
    }


    public RegisterResult login(LoginRequest loginRequest) throws DataAccessException {
        UserData userData = userDao.getUser(loginRequest.getUsername());
        if(userData == null){
            throw new DataAccessException("user name doesn't exist");
        }
        String correctPassword = userData.getPassword();
        String password = loginRequest.getPassword();
        if(BCrypt.checkpw(password, correctPassword)){
            AuthData authData = createAndSaveAuthToken(userData.userName);
            return new RegisterResult(userData.userName, authData.authToken);
        }
        else{
            throw new DataAccessException("wrong password");
        }

    }


    public void logout(String authToken) throws DataAccessException {
        if (!authdao.authTokenExists(authToken)){
            throw new DataAccessException("Invalid authToken");
        }
        authdao.removeAuthToken(authToken);


    }


    public void clear(){
            userDao.clear();
            authdao.clear();
            gameDao.clear();
    }



    private AuthData createAndSaveAuthToken(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);
        authdao.putAuthToken(authData);
        return authData;
    }


    public String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}