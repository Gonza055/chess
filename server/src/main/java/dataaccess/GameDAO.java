package dataaccess;

import chess.ChessGame;
import model.GameRecord;
import java.util.ArrayList;
import java.util.List;

public class GameDAO {

  private List<GameRecord> gameStorage;
  private int gameCounter;

  public GameDAO() {
    initDataStore();
  }

  private void initDataStore() {
    if (gameStorage == null) {
      gameStorage = new ArrayList<>();
      resetCounter();
    }
  }

  private void resetCounter() {
    gameCounter = 1;
  }

  public void deleteAllGames() {
    if (gameStorage != null && !gameStorage.isEmpty()) {
      while (!gameStorage.isEmpty()) {
        gameStorage.remove(0);
      }
    }
    resetCounter();
  }

  public int newGame(String gameName) {
    int newGameID = newGameID();
    ChessGame newChessGame = new ChessGame();
    GameRecord newGame = newGameRecord(newGameID, gameName, newChessGame);
    addGame(newGame);
    return newGameID;
  }

  private int newGameID() {
    return gameCounter++;
  }

  private GameRecord newGameRecord(int id, String name, ChessGame chessGame) {
    return new GameRecord(id, null, null, name, chessGame);
  }

  private void addGame(GameRecord game) {
    boolean added = gameStorage.add(game);
    if (!added) {
      throw new RuntimeException("Failed to add game to storage.");
    }
  }

  public void joinGame(GameRecord existingGame, ChessGame.TeamColor teamColor, String playerName) {
    GameRecord updatedGame = null;

    if (teamColor != null) {
      if (teamColor.equals(ChessGame.TeamColor.WHITE)) {
        updatedGame = updateGame(existingGame, playerName, existingGame.bUsername());
      } else if (teamColor.equals(ChessGame.TeamColor.BLACK)) {
        updatedGame = updateGame(existingGame, existingGame.wUsername(), playerName);
      }
    }

    if (updatedGame != null) {
      replaceGame(existingGame, updatedGame);
    }
  }

  private GameRecord updateGame(GameRecord oldGame, String whitePlayer, String blackPlayer) {
    return new GameRecord(oldGame.gameID(), whitePlayer, blackPlayer, oldGame.gameName(), oldGame.game());
  }

  private void replaceGame(GameRecord oldGame, GameRecord newGame) {
    removeGame(oldGame);
    gameStorage.add(newGame);
  }

  private void removeGame(GameRecord gameToRemove) {
    for (int i = 0; i < gameStorage.size(); i++) {
      if (gameStorage.get(i).gameID() == gameToRemove.gameID()) {
        gameStorage.remove(i);
        break;
      }
    }
  }

  public GameRecord findGame(String gameName) {
    if (gameName != null && !gameName.isEmpty()) {
      for (GameRecord game : gameStorage) {
        if (game != null && game.gameName().equals(gameName)) {
          return game;
        }
      }
    }
    return null;
  }

  public GameRecord findGame(int gameID) {
    for (GameRecord game : gameStorage) {
      if (game != null && game.gameID() == gameID) {
        return game;
      }
    }
    return null;
  }

  public ArrayList<GameRecord> getAllGames() {
    ArrayList<GameRecord> gameListCopy = new ArrayList<>();
    for (GameRecord game : gameStorage) {
      gameListCopy.add(game);
    }
    return gameListCopy;
  }

}