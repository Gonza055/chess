package model;

import chess.ChessGame;
import java.util.Objects;

public final class GameRecord {
  private final int gameID;
  private final String whiteUsername;
  private final String blackUsername;
  private final String gameName;
  private final ChessGame game;

  public GameRecord(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    if (gameName == null || gameName.isEmpty()) {
      throw new IllegalArgumentException("Game name cannot be null or empty.");
    }
    this.gameID = gameID;
    this.whiteUsername = whiteUsername;
    this.blackUsername = blackUsername;
    this.gameName = gameName;
    this.game = game;
  }

  public int gameID() {
    return gameID;
  }

  public String wUsername() {
    return whiteUsername;
  }

  public String bUsername() {
    return blackUsername;
  }

  public String gameName() {
    return gameName;
  }

  public ChessGame game() {
    return game;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof GameRecord)) return false;
    GameRecord that = (GameRecord) o;
    return gameID == that.gameID &&
            Objects.equals(whiteUsername, that.whiteUsername) &&
            Objects.equals(blackUsername, that.blackUsername) &&
            Objects.equals(gameName, that.gameName) &&
            Objects.equals(game, that.game);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gameID, whiteUsername, blackUsername, gameName, game);
  }

  @Override
  public String toString() {
    return "GameRecord{" +
            "gameID=" + gameID +
            ", whiteUsername='" + whiteUsername + '\'' +
            ", blackUsername='" + blackUsername + '\'' +
            ", gameName='" + gameName + '\'' +
            ", game=" + game +
            '}';
  }
}
