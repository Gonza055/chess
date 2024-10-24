package dataaccess;

import chess.ChessGame;
import model.GameRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameDAO {

  private final Map<Integer, GameRecord> gameRecords;
  private int gameIdCounter;

  public GameDAO() {
    this.gameRecords = new HashMap<>();
    this.gameIdCounter = 1;
  }

  public void deleteGames() {
    gameRecords.clear();
    gameIdCounter = 1;
  }

  public int createGame(String gameName) {
    int id = gameIdCounter++;
    GameRecord newGame = new GameRecord(id, null, null, gameName, new ChessGame());
    gameRecords.put(id, newGame);
    return id;
  }

  public void joinGame(int gameID, ChessGame.TeamColor color, String username) {
    GameRecord game = gameRecords.get(gameID);
    if (game == null) return;

    GameRecord updatedGame;
    if (color.equals(ChessGame.TeamColor.WHITE)) {
      updatedGame = new GameRecord(game.gameID(), username, game.bUsername(), game.gameName(), game.game());
    } else {
      updatedGame = new GameRecord(game.gameID(), game.wUsername(), username, game.gameName(), game.game());
    }

    gameRecords.put(gameID, updatedGame);
  }

  public GameRecord findGame(String gameName) {
    return gameRecords.values().stream()
            .filter(game -> game.gameName().equals(gameName))
            .findFirst()
            .orElse(null);
  }

  public GameRecord findGame(int gameID) {
    return gameRecords.get(gameID);
  }

  public List<GameRecord> getAllGames() {
    return gameRecords.values().stream().collect(Collectors.toList());
  }
}

