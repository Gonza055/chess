package webSocketMessages.userCommands;
public class Resign extends UserGameCommand{
    private int gameID;
    public Resign(String authTok, int gID) {
        super(authTok);
        this.commandType = CommandType.RESIGN;
        this.gameID = gID;
    }
    public int getGameID() {
        return gameID;
    }
}
