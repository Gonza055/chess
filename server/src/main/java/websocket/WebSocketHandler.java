package websocket;
import chess.*;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import dataAccess.*;
import model.GameData;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import java.io.IOException;
import java.util.Collection;
@WebSocket (maxIdleTime = 1000000)
public class WebSocketHandler {
    private final String blueColor = "\u001B[34m";
    private final String defaultColor = "\u001B[0m";
    public UserDAO userObj = new SQLUserDAO();
    public AuthDAO authObj = new SQLAuthDAO();
    public GameDAO gameObj = new SQLGameDAO();
    private boolean foundAuth;
    private final ConnectionManager manager = new ConnectionManager();
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, InvalidMoveException {
        UserGameCommand comm = new Gson().fromJson(message, UserGameCommand.class);
        switch (comm.getCommandType()) {
            case JOIN_PLAYER -> {
                JoinPlayer joinPlayerComm = new Gson().fromJson(message, JoinPlayer.class);
                joinGameAsPlayer(joinPlayerComm.getAuthString(), joinPlayerComm.getGameID(), joinPlayerComm.getPlayerColor(), session);
            }
            case JOIN_OBSERVER -> {
                JoinObserver joinObserverComm = new Gson().fromJson(message, JoinObserver.class);
                joinGameAsObserver(joinObserverComm.getAuthString(), joinObserverComm.getGameID(), session);
            }
            case MAKE_MOVE -> {
                MakeMove makeMoveComm = new Gson().fromJson(message, MakeMove.class);
                makeMove(makeMoveComm.getAuthString(), makeMoveComm.getGameID(), makeMoveComm.getMove());
            }
            case RESIGN -> {
                Resign resignComm = new Gson().fromJson(message, Resign.class);
                resign(resignComm.getAuthString(), resignComm.getGameID());
            }
            case LEAVE -> {
                Leave leaveComm = new Gson().fromJson(message, Leave.class);
                leave(leaveComm.getAuthString(), leaveComm.getGameID(), session);
            }
        }
    }

    private void leave(String authTok, int gID, Session sess) throws IOException {
        var leaveMessage = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, "User " + blueColor + authObj.userGet(authTok)+ defaultColor + " has left the game.");
        manager.broadcastAllButOne(leaveMessage, gID, authTok);
        manager.remove(sess);
    }
    private void resign(String authTok, int gameID) throws IOException {
        authCheck(authTok, gameID);
        GameData gameData = gameObj.gameGet(gameID - 1);
        ChessGame game = null;
        if (gameData != null) {
            game = gameData.game();
        }
        if (gameData.game().isGameOver()) {
            var errorMessage = new Error(ServerMessage.ServerMessageType.ERROR, "The game is already over.");
            manager.broadcastUser(errorMessage, gameID, authTok);
        }
        else {
            if (authObj.userGet(authTok).equals(gameData.whiteUsername()) || authObj.userGet(authTok).equals(gameData.blackUsername())) {
                game.setGameOver(true);
                gameObj.gameSet(gameID - 1, gameData);
                var resignMessage = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, "User " + blueColor + authObj.userGet(authTok)+ defaultColor + " has resigned.");
                manager.broadcastAll(resignMessage, gameID);
            } else {
                var errorMessage = new Error(ServerMessage.ServerMessageType.ERROR, "The observer cannot resign.");
                manager.broadcastUser(errorMessage, gameID, authTok);
            }
        }
    }
    private void makeMove(String authTok, int gID, ChessMove moveToMake) throws IOException, InvalidMoveException {
        authCheck(authTok, gID);
        GameData gameData = gameObj.gameGet(gID - 1);
        ChessGame g = null;
        if (gameData != null) {
            g = gameData.game();
        }
        if (gameData.game().isGameOver()) {
            var errMessage = new Error(ServerMessage.ServerMessageType.ERROR, "The game is already over.");
            manager.broadcastUser(errMessage, gID, authTok);
        }
        else if (gameData.game().isInCheckmate(ChessGame.TeamColor.WHITE) || gameData.game().isInCheckmate(ChessGame.TeamColor.BLACK)) {
            var errorMessage = new Error(ServerMessage.ServerMessageType.ERROR, "Game over. You cannot move");
            manager.broadcastUser(errorMessage, gID, authTok);
            g.setGameOver(true);
            gameObj.gameSet(gID - 1, gameData);
        }
        else {
            String currentUser = authObj.userGet(authTok);
            if (g.getTeamTurn() != null && ((g.getTeamTurn() == ChessGame.TeamColor.BLACK && gameData.blackUsername().equals(currentUser)) ||
                    (g.getTeamTurn() == ChessGame.TeamColor.WHITE && gameData.whiteUsername().equals(currentUser)))) {
                Collection <ChessMove> validMoves = g.validMoves(moveToMake.getStartPosition());
                if (validMoves != null && validMoves.contains(moveToMake)) {
                    g.makeMove(moveToMake);
                    gameObj.gameSet(gID - 1, gameData);
                    var loadGameMessage = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, g, g.getTeamTurn());
                    manager.broadcastAll(loadGameMessage, gID);
                    var moveMessage = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, "User " + blueColor + authObj.userGet(authTok)+ defaultColor + " has made a move.");
                    manager.broadcastAllButOne(moveMessage, gID, authTok);
                    if (gameData.game().isInCheckmate(ChessGame.TeamColor.WHITE) || gameData.game().isInCheckmate(ChessGame.TeamColor.BLACK)) {
                        if (gameData.game().isInCheckmate(ChessGame.TeamColor.WHITE)) {
                            var errorMessage = new Error(ServerMessage.ServerMessageType.ERROR, "Black wins by checkmate.");
                            manager.broadcastAll(errorMessage, gID);
                        }
                        else if (gameData.game().isInCheckmate(ChessGame.TeamColor.BLACK)){
                            var errorMessage = new Error(ServerMessage.ServerMessageType.ERROR, "White wins by checkmate.");
                            manager.broadcastAll(errorMessage, gID);
                        }
                        g.setGameOver(true);
                        gameObj.gameSet(gID - 1, gameData);
                    }
                    else if (gameData.game().isInCheck(ChessGame.TeamColor.WHITE) || gameData.game().isInCheck(ChessGame.TeamColor.BLACK)) {
                        if (gameData.game().isInCheck(ChessGame.TeamColor.WHITE)) {
                            var errorMessage = new Error(ServerMessage.ServerMessageType.ERROR, "White is now in check.");
                            manager.broadcastAll(errorMessage, gID);
                        }
                        else if (gameData.game().isInCheck(ChessGame.TeamColor.BLACK)){
                            var errorMessage = new Error(ServerMessage.ServerMessageType.ERROR, "Black is now in check.");
                            manager.broadcastAll(errorMessage, gID);
                        }
                    }
                }
                else {
                    var errMessage = new Error(ServerMessage.ServerMessageType.ERROR, "Invalid move");
                    manager.broadcastUser(errMessage, gID, authTok);
                }
            }
            else {
                var errMessage = new Error(ServerMessage.ServerMessageType.ERROR, "It's not your turn.");
                manager.broadcastUser(errMessage, gID, authTok);
            }
        }
    }
    private void joinGameAsPlayer(String authTok, int gID, ChessGame.TeamColor plyrColor, Session sess) throws IOException {
        manager.add(authTok, sess, gID);
        authCheck(authTok, gID);
        GameData gamData = gameObj.gameGet(gID - 1);
        ChessGame game = null;
        if (gamData != null) {
            game = gamData.game();
        }
        gameCheck(authTok, gID, gamData);
        if (gamData.game().isGameOver()) {
            var errMessage = new Error(ServerMessage.ServerMessageType.ERROR, "The game is already over.");
            manager.broadcastUser(errMessage, gID, authTok);
        }
        else {
            String reqUser = authObj.userGet(authTok);
            String whiteUser = gamData.whiteUsername();
            String blackUser = gamData.blackUsername();
            if ((plyrColor == ChessGame.TeamColor.BLACK && reqUser.equals(blackUser)) || (plyrColor == ChessGame.TeamColor.WHITE && reqUser.equals(whiteUser))) {
                var loadGMessage = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, game, plyrColor);
                manager.broadcastUser(loadGMessage, gID, authTok);
            } else {
                var errMessage = new Error(ServerMessage.ServerMessageType.ERROR, "You are not on that team.");
                manager.broadcastUser(errMessage, gID, authTok);
            }

            var joinMessage = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, "User " + blueColor + authObj.userGet(authTok)+ defaultColor + " has joined the game.");
            manager.broadcastAllButOne(joinMessage, gID, authTok);
        }
    }
    private void gameCheck(String authTok, int gID, GameData gamData) throws IOException{
        ChessGame g = null;
        if (gamData != null) {
            g = gameObj.gameGet(gID - 1).game();
            if (g != null && gamData.gameID() == gID) {

            } else {
                var errMessage = new Error(ServerMessage.ServerMessageType.ERROR, "Bad GameID");
                manager.broadcastUser(errMessage, gID, authTok);
            }
        } else {
            var errorMessage = new Error(ServerMessage.ServerMessageType.ERROR, "Bad GameID");
            manager.broadcastUser(errorMessage, gID, authTok);
        }
    }
    private void authCheck(String authToken, int gameID) throws IOException {
        foundAuth = false;
        for (int i=0; i <= authObj.sizeGet(); i++){
            if (authObj.getAuthWID(i) != null && authObj.getAuthWID(i).authToken().equals(authToken)) {
                foundAuth = true;
            }
        }

        if (!foundAuth) {
            var errorMessage = new Error(ServerMessage.ServerMessageType.ERROR, "Bad AuthToken");
            manager.broadcastUser(errorMessage, gameID, authToken);
        }
    }
    private void joinGameAsObserver(String authTok, int gID, Session sess) throws IOException {
        manager.add(authTok, sess, gID);
        authCheck(authTok, gID);
        GameData gameData = gameObj.gameGet(gID - 1);
        gameCheck(authTok, gID, gameData);
        if (gameData.game().isGameOver()) {
            var errorMessage = new Error(ServerMessage.ServerMessageType.ERROR, "The game is already over. Type 'help' for a list of commands.");
            manager.broadcastUser(errorMessage, gID, authTok);
        }

        else {
            if (foundAuth) {
                var observeMessage = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameObj.gameGet(gID - 1).game(), null);
                manager.broadcastUser(observeMessage, gID, authTok);

                var joinMessage = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, "User " + blueColor + authObj.userGet(authTok)
                        + defaultColor + " has joined the game.");
                manager.broadcastAllButOne(joinMessage, gID, authTok);
            }
        }
    }

}