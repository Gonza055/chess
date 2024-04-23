package webSocketMessages.userCommands;
public class Leave extends UserGameCommand{
    private int gameID;
    public Leave(String authTok, int gID) {
        super(authTok);
        this.commandType = CommandType.LEAVE;
        this.gameID = gID;
    }
    public int getGameID() {
        return gameID;
    }
}
