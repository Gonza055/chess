package request;
public class JoinGameRequest {
    public String playerColor;
    public int gameID;
    public JoinGameRequest(String playerColor, int gameID) {
        this.gameID = gameID;
        this.playerColor = playerColor;
    }
    public String getPlayerColor() {
        return playerColor;
    }
    public int getGameID() {
        return gameID;
    }
}
