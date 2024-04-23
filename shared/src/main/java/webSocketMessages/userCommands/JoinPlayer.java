package webSocketMessages.userCommands;
import chess.ChessGame;
public class JoinPlayer extends UserGameCommand{
    private int gameID;
    private ChessGame.TeamColor playerColor;
    public JoinPlayer(String authTok, int gID, ChessGame.TeamColor plyrColor) {
        super(authTok);
        this.commandType = CommandType.JOIN_PLAYER;
        this.gameID = gID;
        this.playerColor = plyrColor;}
    public int getGameID() {
        return gameID;
    }
    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
}
