package dao;

import chess.ChessGame;
import dataaccess.DataAccessException;
import models.AuthData;
import models.GameData;
import service.GameService;

import java.sql.SQLException;

public class SQLAuthdao implements Authdao{

    public SQLAuthdao() throws DataAccessException {
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
            throw new DataAccessException("could not connect to Database", e);
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `userName` varchar(255) ,
              `authToken` varchar(255) NOT NULL,
              PRIMARY KEY (`authToken`)
            )"""
    };
    @Override
    public void putAuthToken(AuthData authToken) throws DataAccessException {
        try {
            String statement = "INSERT INTO auth (authToken, userName) VALUES (?, ?)";
            DatabaseManager.executeUpdate(statement, authToken.authToken, authToken.username);
        }
        catch (DataAccessException e){
            throw new DataAccessException("couldn't create a new auth", e);
        }
    }

    @Override
    public void clear() {
        try {
            DatabaseManager.executeUpdate("TRUNCATE TABLE auth");
        }
        catch (DataAccessException e){
            System.err.println("could not clear");
        }
    }

    @Override
    public void removeAuthToken(String authData) throws DataAccessException {
        try {
            String statement = "DELETE FROM auth WHERE authToken = ?";
            DatabaseManager.executeUpdate(statement, authData);
        }
        catch (DataAccessException e){
            throw new DataAccessException("couldn't delete a new authToken", e);
        }
    }

    @Override
    public boolean authTokenExists(String authToken) throws DataAccessException {
        AuthData authData = getAuthDataByToken(authToken);
        return authData != null;
    }

    @Override
    public AuthData getAuthDataByToken(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM auth WHERE authToken=?";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                try (var queryResults = preparedStatement.executeQuery()){
                    if(!queryResults.next()) return null;
                    var username = queryResults.getString("userName");
                    var authT = queryResults.getString("authToken");

                    AuthData authData = new AuthData(authT, username);
                    return authData;
                }
            }
        }
        catch (DataAccessException | SQLException e) {
            throw new DataAccessException("DataBase Query failed", e);
        }
    }
}
