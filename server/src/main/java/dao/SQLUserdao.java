package dao;

import dataaccess.DataAccessException;
import models.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLUserdao implements Usrdao{

    private int updateUser(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement_prepared = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) statement_prepared.setString(i + 1, p);
                    else if (param instanceof Integer p) statement_prepared.setInt(i + 1, p);
                    else if (param == null) statement_prepared.setNull(i + 1, NULL);
                }

                statement_prepared.executeUpdate();

                var rs = statement_prepared.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException("DataBase Query failed");
        }
    }


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
    public UserData getUser(String userName) {
        return null;
    }


    @Override
    public void putUser(UserData userData) {
        try {
            String hashedPassword = hashPassword(userData.password);
            String statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            updateUser(statement, userData.userName, hashedPassword, userData.email);
        }
        catch (DataAccessException e){
            System.err.println("Not a valid username");
        }
    }

    @Override
    public void clear() throws DataAccessException {
            updateUser("TRUNCATE users");
    }

    @Override
    public String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
