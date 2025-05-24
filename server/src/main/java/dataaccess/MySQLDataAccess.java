package dataaccess;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;


import java.sql.*;

public abstract class MySQLDataAccess implements DataAccess {
    private final Gson gson = new Gson();

    public MySQLDataAccess() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (\n" +
                    "username VARCHAR (255) PRIMARY KEY, \n" +
                    "password VARCHAR (255) NOT NULL, \n" +
                    "email VARCHAR (255) NOT NULL\n" +
                    ");");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS auth (\n" +
                    "authToken VARCHAR (255) PRIMARY KEY, \n" +
                    "username VARCHAR (255) NOT NULL,\n" +
                    "FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE\n" +
                    ");");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS games (\n" +
                    "gameID INT PRIMARY KEY AUTO_INCREMENT,\n" +
                    "whiteUsername VARCHAR (255),\n" +
                    "blackUsername VARCHAR (255),\n" +
                    "gameName VARCHAR (255) NOT NULL,\n" +
                    "game TEXT,\n" +
                    "FOREIGN KEY (whiteUsername) REFERENCES users(username) ON DELETE SET NULL,\n" +
                    "FOREIGN KEY (blackUsername) REFERENCES users(username) ON DELETE SET NULL\n" +
                    ");");
        } catch (SQLException e) {
            throw new DataAccessException("failed to create tables " + e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
            }
            return null;
        } catch (SQLException e) {
            throw new DataAccessException("failed to get user " + e.getMessage());
        }
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO users(username, password, email) VALUES (?, ?, ?)")) {
            String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt(12));
            stmt.setString(1, user.username());
            stmt.setString(2, hashedPassword);
            //stmt.setString(2, user.hashedPassword());
            stmt.setString(3, user.email());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("failed to create user " + e.getMessage());
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM auth WHERE authToken = ?")) {
            stmt.setString(1, authToken);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new AuthData(rs.getString("authToken"), rs.getString("username"));
            }
            return null;
        } catch (SQLException e) {
            throw new DataAccessException("failed to get auth " + e.getMessage());
        }
    }

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO auth(authToken, username) VALUES (?, ?)")) {
            stmt.setString(1, auth.authToken());
            stmt.setString(2, auth.username());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("failed to create auth " + e.getMessage());
        }
    }

}