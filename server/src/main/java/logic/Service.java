package logic;

import chess.ChessGame;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

import model.auth.AuthRecord;
import model.game.GameRecord;
import model.auth.LoginRequest;
import model.user.UserRecord;

import java.util.ArrayList;

public class Service {

  private AuthDAO authenticationDataStore;
  private GameDAO gameDataStore;
  private UserDAO userDataStore;

  public Service() {
    this.authenticationDataStore = new AuthDAO();
    this.gameDataStore = new GameDAO();
    this.userDataStore = new UserDAO();
  }

  public AuthRecord register(UserRecord user) throws Exception {
    if (isUserIncomplete(user)) {
      handleInvalidUser();
    }

    if (userAlreadyExists(user.username())) {
      handleUserAlreadyExists();
    }

    this.userDataStore.newUser(user);
    return this.authenticationDataStore.newAuth(user.username());
  }

  private boolean isUserIncomplete(UserRecord user) {
    return user == null || user.username() == null || user.password() == null || user.email() == null;
  }

  private void handleInvalidUser() throws Exception {
    throw new Exception("Error: invalid user", 400);
  }

  private boolean userAlreadyExists(String username) {
    return this.userDataStore.findUser(username) != null;
  }

  private void handleUserAlreadyExists() throws Exception {
    throw new Exception("Error: user already exists", 403);
  }

  public AuthRecord login(LoginRequest request) throws Exception {
    if (isLoginRequestInvalid(request)) {
      throw new Exception("Error: invalid login request", 400);
    }

    UserRecord userRecord = findUserInDataStore(request.username());
    if (isLoginUnauthorized(userRecord, request.password())) {
      throw new Exception("Error: unauthorized", 401);
    }

    return this.authenticationDataStore.newAuth(request.username());
  }

  private boolean isLoginRequestInvalid(LoginRequest request) {
    return request == null || request.username() == null || request.password() == null;
  }

  private UserRecord findUserInDataStore(String username) {
    return this.userDataStore.findUser(username);
  }

  private boolean isLoginUnauthorized(UserRecord user, String password) {
    return user == null || !user.password().equals(password);
  }

  public AuthRecord verify(String authToken) throws Exception {
    if (isTokenInvalid(authToken)) {
      throw new Exception("Error: invalid token", 401);
    }

    AuthRecord user = findAuthRecord(authToken);
    if (user == null) {
      throw new Exception("Error: unauthorized", 401);
    }
    return user;
  }

  private boolean isTokenInvalid(String authToken) {
    return authToken == null || authToken.isEmpty();
  }

  private AuthRecord findAuthRecord(String authToken) {
    return this.authenticationDataStore.addAuth(authToken);
  }

  public void joinGame(String authToken, ChessGame.TeamColor playerColor, int gameID) throws Exception {
    AuthRecord user = verify(authToken);

    if (isInvalidTeamColor(playerColor)) {
      throw new Exception("Error: invalid team color", 400);
    }

    GameRecord game = lookGameById(gameID);
    if (game == null) {
      throw new Exception("Error: game not found", 400);
    }

    if (isTeamColorAlreadyTaken(game, playerColor)) {
      throw new Exception("Error: team color already taken", 403);
    }

    this.gameDataStore.joinGame(game, playerColor, user.username());
  }

  private boolean isInvalidTeamColor(ChessGame.TeamColor color) {
    return color == null || (!color.equals(ChessGame.TeamColor.WHITE) && !color.equals(ChessGame.TeamColor.BLACK));
  }

  private GameRecord lookGameById(int gameID) {
    return this.gameDataStore.findGame(gameID);
  }

  private boolean isTeamColorAlreadyTaken(GameRecord game, ChessGame.TeamColor color) {
    if (color.equals(ChessGame.TeamColor.WHITE)) {
      return game.wUsername() != null;
    } else {
      return game.bUsername() != null;
    }
  }

  public int newGame(String authToken, String gameName) throws Exception {
    verify(authToken);

    if (gameNameAlreadyExist(gameName)) {
      throw new Exception("Error: game name already exists", 400);
    }

    return this.gameDataStore.newGame(gameName);
  }

  private boolean gameNameAlreadyExist(String gameName) {
    return this.gameDataStore.findGame(gameName) != null;
  }

  public void logout(String authToken) throws Exception {
    AuthRecord user = verify(authToken);
    this.authenticationDataStore.deleteSingleAuth(authToken);
  }

  public ArrayList<GameRecord> listGames(String authToken) throws Exception {
    verify(authToken);
    return this.gameDataStore.getAllGames();
  }

  public void deleteDB() {
    this.authenticationDataStore.deleteAllAuths();
    this.gameDataStore.deleteAllGames();
    this.userDataStore.deleteAllUsers();
  }
}