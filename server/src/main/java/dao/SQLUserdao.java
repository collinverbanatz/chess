package dao;

import dataaccess.DataAccessException;
import models.UserData;

import java.sql.SQLException;

public class SQLUserdao implements Usrdao{


    public SQLUserdao() {
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
            try {
                throw new DataAccessException("wrong");
            } catch (DataAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  pet (
              `username` varchar(255) NOT NULL,
              'password' varchar(255) NOT NULL,
              'email' varchar(255) NOT NULL,
              PRIMARY KEY (`username`),
            )"""
    };

    @Override
    public UserData getUser(String userName) throws DataAccessException {
        return null;
    }

    @Override
    public void putUser(UserData userData) {

    }

    @Override
    public void clear() {

    }
}
