package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exception.ResponseException;
import model.AuthRecord;
import model.GameRecord;
import model.LoginRequest;
import model.UserRecord;

import java.util.List;

public class Service {

  private AuthDAO authDAO;
  private GameDAO gameDAO;
  private UserDAO userDAO;

  public Service() {
    this.authDAO = new AuthDAO();  // HashMap-based DAOs
    this.gameDAO = new GameDAO();  // HashMap-based DAOs
    this.userDAO = new UserDAO();  // HashMap-based DAOs
  }

  public AuthRecord registerUser(UserRecord user) throws ResponseException {
    // Ensure user has all required fields (accessing username and password through info())
    if (user.info().password() == null || user.info().username() == null || user.email() == null) {
      throw new ResponseException("Error: bad request", 400);
    }

    // Check if user already exists
    UserRecord existingUser = this.userDAO.findUser(user.info().username());
    if (existingUser != null) {
      throw new ResponseException("Error: already taken", 403);
    }

    // Create the user and authenticate
    this.userDAO.createUser(user);
    return this.authDAO.newAuth(user.info().username());
  }

  public AuthRecord loginUser(LoginRequest login) throws ResponseException {
    // Verify user credentials (accessing username and password through info())
    UserRecord userRecord = this.userDAO.findUser(login.username());
    if (userRecord == null || !userRecord.info().password().equals(login.password())) {
      throw new ResponseException("Error: unauthorized", 401);
    }

    // Create and return a new auth token
    return this.authDAO.newAuth(login.username());
  }

  public AuthRecord verifyUser(String authToken) throws ResponseException {
    if (authToken == null) {
      throw new ResponseException("Error: unauthorized", 401);
    }

    AuthRecord authRecord = this.authDAO.getAuth(authToken);
    if (authRecord == null) {
      throw new ResponseException("Error: unauthorized", 401);
    }

    return authRecord;
  }

  public void joinGame(String authToken, ChessGame.TeamColor playerColor, int gameID) throws ResponseException {
    AuthRecord user = this.verifyUser(authToken);

    // Validate the player's color selection
    if (playerColor == null || (!playerColor.equals(ChessGame.TeamColor.WHITE) && !playerColor.equals(ChessGame.TeamColor.BLACK))) {
      throw new ResponseException("Error: bad request", 400);
    }

    // Retrieve the game
    GameRecord game = this.gameDAO.findGame(gameID);
    if (game == null) {
      throw new ResponseException("Error: bad request", 400);
    }

    // Check if the color is already taken
    if (playerColor.equals(ChessGame.TeamColor.WHITE)) {
      if (game.whiteUsername() != null) {
        throw new ResponseException("Error: already taken", 403);
      }
    } else {
      if (game.blackUsername() != null) {
        throw new ResponseException("Error: already taken", 403);
      }
    }

    // Join the game
    this.gameDAO.joinGame(gameID, playerColor, user.username());
  }

  public int createGame(String authToken, String gameName) throws ResponseException {
    this.verifyUser(authToken);

    // Check if the game name already exists
    if (this.gameDAO.findGame(gameName) != null) {
      throw new ResponseException("Error: bad request", 400);
    }

    // Create the game and return the game ID
    return this.gameDAO.createGame(gameName);
  }

  public void logoutUser(String authToken) throws ResponseException {
    this.verifyUser(authToken);
    this.authDAO.deleteAuth(authToken);
  }

  // Changed to return List<GameRecord> instead of ArrayList<GameRecord> to match the HashMap structure
  public List<GameRecord> listGames(String authToken) throws ResponseException {
    this.verifyUser(authToken);
    return this.gameDAO.getAllGames();  // Assuming GameDAO now returns a List
  }

  public void deleteDB() {
    this.authDAO.deleteAllAuths();
    this.gameDAO.deleteGames();
    this.userDAO.deleteUsers();
  }
}
