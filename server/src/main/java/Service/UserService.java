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


//    public RegisterResult register(RegisterRequest registerRequest) {

//    }
    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        UserData userData = userDao.getUser(loginRequest.username);
        String correctPassword = userData.getPassword();
        String password = loginRequest.password;
        if(correctPassword.equals(password)){
            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(authToken, userData.userName);
            authDao.putAuthToken(authData);
            return new LoginResult(userData.userName, authToken);
        }
        else{
            throw new IllegalStateException("wrong password");
        }

    }
//    public void logout(LogoutRequest logoutRequest) {

//    }




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
