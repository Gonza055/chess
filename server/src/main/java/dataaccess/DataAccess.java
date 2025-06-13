package dataaccess;

import model.*;

import java.util.Objects;

public interface DataAccess {
    UserData getUser(String username) throws DataAccessException;
    void createUser(UserData user) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void createAuth(AuthData auth) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;

    GameData[] getAllGames() throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    void createGame(GameData game) throws DataAccessException;
    void updateGame(int gameID, GameData game) throws DataAccessException;

    void clear() throws DataAccessException;
    void deleteAllUsers() throws DataAccessException;
    void deleteAllAuth() throws DataAccessException;
    void deleteAllGames() throws DataAccessException;

    int generateGameID() throws DataAccessException;

    default boolean isObserver(int gameID, String username) throws DataAccessException {
        GameData game = getGame(gameID);
        if (game == null) return false;
        if (Objects.equals(game.whiteUsername(), username) || Objects.equals(game.blackUsername(), username)) {
            return false;
        }
        UserData user = getUser(username);
        return user != null;
    }
}
