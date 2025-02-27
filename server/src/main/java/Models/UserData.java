package Models;

//class to clas out userName and Password out of the object after being converted from gson
public class UserData {
    public String userName;
    public String password;
    public String email;

    public UserData(String userName, String password, String email) {
        this.userName = userName;
        this.password = password;
        this.email = email;
    }

    public String getuserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail(){return email;}
}
