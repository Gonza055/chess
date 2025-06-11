package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessageNotification;
import websocket.messages.ServerMessageError;
import dataaccess.DataAccessException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@WebSocket
public class WebSocketServer {
    private final GameService gameService;
    private final Map<String, Session> sessions = new HashMap<>();
    private final Map<Integer, Map<String, Session>> gameSessions = new HashMap<>();
    private final Map<Session, String> sessionAuthTokens = new HashMap<>();
    private final Map<String, Integer> authTokenGameIds = new HashMap<>();

    private final Gson gson = new Gson();

    public WebSocketServer(GameService gameService) {
        this.gameService = gameService;
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("New WebSocket connected: " + session.getRemoteAddress());
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        String authToken = sessionAuthTokens.remove(session);

        if (authToken != null) {
            Integer gameID = authTokenGameIds.remove(authToken);

            if (gameID != null) {
                Map<String, Session> gameSessionMap = gameSessions.get(gameID);
                if (gameSessionMap != null) {
                    gameSessionMap.remove(authToken);
                    if (gameSessionMap.isEmpty()) {
                        gameSessions.remove(gameID);
                    }
                }
            }
            sessions.remove(authToken);

            System.out.println("WebSocket closed for AuthToken: " + authToken + ", Reason: " + reason);
        } else {
            System.out.println("WebSocket closed for unknown session, Reason: " + reason);
        }
    }

    @OnWebSocketMessage
    public void OnMessage(Session session, String message) throws IOException {
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
        String authToken = getAuthTokenFromSession(session);
        Integer gameID = getGameIDFromSession(session);
        try {
            switch (command.getCommandType()) {
                case CONNECT:
                    break;
                case MAKE_MOVE:
                    ChessMove move = gson.fromJson(message, ChessMove.class);
                    //ChessGame.makeMove(move);
                    broadcastLoadGame(gameID, authToken);
                    break;
                case RESIGN:
                    break;
                case LEAVE:
                    sessions.remove(authToken);
                    gameSessions.get(gameID).remove(authToken);
                    broadcastNotification(gameID, authToken + " left the game.", session);
            }
        } catch (Exception e){
            sendError(session, "Error Processing " + e.getMessage());
        }
    }


    private void sendMessage(Session session, ServerMessage message, String authToken, Integer gameID) {
        if (session.isOpen()) {
            try {
                if (message.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                    String json = gson.toJson(message) + ",\"game\":" + gameService.getGameState(gameID, authToken);
                    session.getRemote().sendString(json);
                } else {
                    session.getRemote().sendString(gson.toJson(message));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DataAccessException e) {
                e.printStackTrace();
                System.out.println("Error sending message: " + e.getMessage());
            }
        }
    }

    private void broadcastNotification(Integer gameID, String message, Session excludedSession) {
        Map<String, Session> gameSession = this.gameSessions.get(gameID);
        if (gameSession != null) {
            for (Session session : gameSession.values()) {
                if (session != excludedSession && session.isOpen()) {
                    sendMessage(session, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION), null, gameID);
                    try {
                        session.getRemote().sendString(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
    private void broadcastLoadGame(Integer gameID, String authToken) {
        Map<String, Session> gameSession = this.gameSessions.get(gameID);
        if (gameSession != null) {
            for (Session session : gameSession.values()) {
                sendMessage(session, new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME), authToken, gameID);
            }
        }
    }

    private void sendError(Session session, String errorMessage) {
        if (session.isOpen()) {
            try {
                session.getRemote().sendString(errorMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getAuthTokenFromSession(Session session) {
        return session.getUpgradeRequest().getParameterMap().get("AuthToken").get(0);
    }

    private Integer getGameIDFromSession(Session session) {
        return Integer.parseInt(session.getUpgradeRequest().getParameterMap().get("GameID").get(0));
    }

    public void start(int port) {
        spark.Spark.port(port);
        spark.Spark.webSocket("/ws", WebSocketServer.class);
        spark.Spark.init();
    }

    public void stop() {
        spark.Spark.stop();
        spark.Spark.awaitStop();
        sessions.clear();
        gameSessions.clear();
        sessionAuthTokens.clear();
        authTokenGameIds.clear();
        System.out.println("WebSocket server stopped.");
    }



}