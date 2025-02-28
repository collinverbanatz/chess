package Service;

import DOA.AuthDOA;
import DOA.MemoryAuthDAO;
import DOA.MemoryUserDOA;
import DOA.UsrDOA;
import Models.AuthData;
import Models.UserData;
import dataaccess.DataAccessException;
import server.UserHandler;

import java.util.UUID;

public class UserService {
    UsrDOA userDao = new MemoryUserDOA();
    AuthDOA authDao = new MemoryAuthDAO();


    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        UserData userData = userDao.getUser(registerRequest.username);
        if(userData != null){
            throw new IllegalStateException("username all ready exists");
        }
        userDao.putUser(new UserData(registerRequest.username, registerRequest.password, registerRequest.email));
        AuthData authData = createAndSaveAuthToken(registerRequest.username);
        return new RegisterResult(registerRequest.username, authData.authToken);
    }


    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        UserData userData = userDao.getUser(loginRequest.username);
        if(userData == null){
            throw new IllegalStateException("user name doesn't exist");
        }
        String correctPassword = userData.getPassword();
        String password = loginRequest.password;
        if(correctPassword.equals(password)){
            AuthData authData = createAndSaveAuthToken(userData.userName);
            return new LoginResult(userData.userName, authData.authToken);
        }
        else{
            throw new IllegalStateException("wrong password");
        }

    }


    public void logout(String authToken) throws DataAccessException {
        if (!authDao.authTokenExists(authToken)){
            throw new DataAccessException("Invalid authToken");
        }
        authDao.removeAuthToken(authToken);


    }


    public void clear(){
        userDao.clear();
        authDao.clear();
    }


    private AuthData createAndSaveAuthToken(String username){
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);
        authDao.putAuthToken(authData);
        return authData;
    }

    public static class LogoutRequest{
        private String authToken;

        public LogoutRequest(String authToken) {
            this.authToken = authToken;
        }

        public String getAuthToken() {
            return authToken;
        }

        public void setAuthToken(String authToken) {
            this.authToken = authToken;
        }
    }






    public static class RegisterResult{
        private String username;
        private String authToken;

        public RegisterResult(String username, String authToken) {
            this.username = username;
            this.authToken = authToken;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAuthToken() {
            return authToken;
        }

        public void setAuthToken(String authToken) {
            this.authToken = authToken;
        }
    }

    public static class RegisterRequest{
        private String username;
        private String password;
        private String email;

        public RegisterRequest(String username, String password, String email) {
            this.username = username;
            this.password = password;
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }





    public static class LoginResult{
        private String username;
        private String authToken;

        public LoginResult(String username, String authToken) {
            this.username = username;
            this.authToken = authToken;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAuthToken() {
            return authToken;
        }

        public void setAuthToken(String authToken) {
            this.authToken = authToken;
        }
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getPassword() {
            return password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }



}
