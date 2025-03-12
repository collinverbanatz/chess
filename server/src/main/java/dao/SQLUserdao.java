package dao;

import dataaccess.DataAccessException;
import models.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLUserdao implements Usrdao{

    public SQLUserdao() throws DataAccessException {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try (var conn = DatabaseManager.getConnection()) {
            for (var state : createStatements) {
                try (var preparedStatement = conn.prepareStatement(state)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("could not connect to Database");
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  users (
              `username` varchar(255) NOT NULL,
              `password` varchar(255) NOT NULL,
              `email` varchar(255) NOT NULL,
              PRIMARY KEY (`username`)
            )"""
    };

    @Override
    public UserData getUser(String userName) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM users WHERE username=?";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, userName);
                try (var queryResults = preparedStatement.executeQuery()){
                    if(!queryResults.next()) {
                        return null;
                    }
                    var username = queryResults.getString("username");
                    var password = queryResults.getString("password");
                    var email = queryResults.getString("email");
                    UserData gotUser = new UserData(username, password, email);
                    return gotUser;
                }
            }
        }
        catch (DataAccessException | SQLException e) {
            throw new DataAccessException("DataBase Query failed");
        }
    }


    @Override
    public void putUser(UserData userData) throws DataAccessException {
        try {
            String hashedPassword = userData.password;
            String statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            DatabaseManager.executeUpdate(statement, userData.userName, hashedPassword, userData.email);
        }
        catch (DataAccessException e){
            throw new DataAccessException("Not a valid username");
        }
    }

    @Override
    public void clear() {
        try {
            DatabaseManager.executeUpdate("TRUNCATE TABLE users");
        }
        catch (DataAccessException e){
            System.err.println("could not clear");
        }
    }
}
