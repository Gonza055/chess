package webSocketMessages.userCommands;
public class JoinObserver extends UserGameCommand{
    private int gameID;
    public JoinObserver(String authTok, int gID) {
        super(authTok);
        this.commandType = CommandType.JOIN_OBSERVER;
        this.gameID = gID;
    }
    public int getGameID() {
        return gameID;
    }
}
