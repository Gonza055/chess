package dataAccess;
import java.util.Properties;
import java.sql.*;
public class DatabaseManager {
    private static final String databaseName;
    private static final String user;
    private static final String password;
    private static final String connectionUrl;
    static {
        try {
            try (var propstream= Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propstream == null) throw new Exception("Unable to load db.properties");
                Properties prop = new Properties();
                prop.load(propstream);
                databaseName = prop.getProperty("db.name");
                user = prop.getProperty("db.user");
                password = prop.getProperty("db.password");

                var host = prop.getProperty("db.host");
                var port = Integer.parseInt(prop.getProperty("db.port"));
                connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }
    public static void createDatabase() throws DataAccessException {
        try {
            var sttmnt= "CREATE DATABASE IF NOT EXISTS " + databaseName;
            var con= DriverManager.getConnection(connectionUrl, user, password);
            try (var prepsttmnt= con.prepareStatement(sttmnt)) {
                prepsttmnt.executeUpdate();
            }
            con.setCatalog(databaseName);
            String createGamesTable = "CREATE TABLE IF NOT EXISTS games (" +
                    "ID INT NOT NULL, " +
                    "gameID INT NOT NULL PRIMARY KEY, " +
                    "whiteUsername VARCHAR(255), " +
                    "blackUsername VARCHAR(255), " +
                    "gameName VARCHAR(255), " +
                    "game JSON" + ")";
            try (var prprdsttmnt= con.prepareStatement(createGamesTable)) {
                prprdsttmnt.executeUpdate();
            }
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "ID INT NOT NULL PRIMARY KEY, " +
                    "username VARCHAR(255), " +
                    "password VARCHAR(255), " +
                    "email VARCHAR(255)" + ")";
            try (var prprdsttmnt = con.prepareStatement(createUsersTable)){
                prprdsttmnt.executeUpdate();
            }
            String createAuthTable = "CREATE TABLE IF NOT EXISTS auth (" +
                    "ID INT NOT NULL PRIMARY KEY, " +
                    "authToken VARCHAR(255), " +
                    "username VARCHAR(255)" + ")";
            try (var prprdsttmnt = con.prepareStatement(createAuthTable)){
                prprdsttmnt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
    static Connection getConnection() throws DataAccessException {
        try {
            var con = DriverManager.getConnection(connectionUrl, user, password);
            con.setCatalog(databaseName);
            return con;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}