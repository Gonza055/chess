package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import websocket.messages.ServerMessageError;
import websocket.messages.ServerMessageNotification;

import java.net.URI;
import java.util.concurrent.TimeUnit;

public class WebSocketClientManager {
    private final Gson gson = new Gson();
    private clientListener listener;
    private WebSocketClient client;
    private WebSocketClientEndpoint endpoint;
    private boolean isRunning = false;

    private String currentAuthToken;
    private Integer currentGameID;

    public interface clientListener {
        void onGameUpdate(ChessGame game);
        void onNotification(String message);
        void onError(String errorMessage);
    }

    public WebSocketClientManager() {
        this.client = new WebSocketClient();
        try {
            client.start();
            isRunning = true;
        } catch (Exception e) {
            System.err.println("Error starting WebSocketClient: " + e.getMessage());
        }
    }

    public void setListener(clientListener listener) {
        this.listener = listener;
    }


    public void connect(String wsUrl, String authToken, String gameId) throws Exception {
        this.currentAuthToken = authToken;
        this.currentGameID = Integer.parseInt(gameId);

        URI uri = new URI(wsUrl);

        endpoint = new WebSocketClientEndpoint();
        endpoint.setMessageHandler(this::handleMessage);

        ClientUpgradeRequest request = new ClientUpgradeRequest();

        client.connect(endpoint, uri, request).get(5, TimeUnit.SECONDS);

        sendCommand(new UserGameCommand(UserGameCommand.CommandType.CONNECT, currentAuthToken, currentGameID));
    }

    public void disconnect() {
        try {
            client.stop();
        } catch (Exception e) {
            System.err.println("Error stopping websocket client: " + e.getMessage());
        }
        client = null;
        endpoint = null;
    }

    public void sendCommand(UserGameCommand command) {
        if (endpoint != null) {
            try {
                endpoint.sendMessage(gson.toJson(command));
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        }
    }
    private void handleMessage(String message) {
        JsonObject json = gson.fromJson(message, JsonObject.class);
        ServerMessage.ServerMessageType type = ServerMessage.ServerMessageType.valueOf(json.get("serverMessageType").getAsString());
        switch (type) {
            case LOAD_GAME -> {
                LoadGameMessage loadGameMsg = gson.fromJson(message, LoadGameMessage.class);
                if (listener != null) {
                    listener.onGameUpdate(loadGameMsg.getGame());
                }
            }
            case NOTIFICATION -> {
                ServerMessageNotification notificationmsg = gson.fromJson(message, ServerMessageNotification.class);
                if (listener != null) {
                    listener.onNotification(notificationmsg.getMessage());
                }
            }
            case ERROR -> {
                ServerMessageError errorMsg = gson.fromJson(message, ServerMessageError.class);
                if (listener != null) {
                    listener.onError(errorMsg.getErrorMessage());
                }
            }
        }
    }
}

