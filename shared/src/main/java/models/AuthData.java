package models;

// class to get authToken
public class AuthData {
    public String authToken;
    public String username;

    public AuthData(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername(){return username;}
}
