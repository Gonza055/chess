package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.net.URI;

public class WebSocketClient {
    private final Gson gson = new Gson();
    private clientListener listener;
    private WebSocketClient client;
    private WebSocketClientEndpoint endpoint;

    public interface clientListener {
        void onGameUpdate(ChessGame game);
        void onNotification(String message);
        void onError(String errorMessage);
    }

    public WebSocketClient() {
    }

    public void setListener(clientListener listener) {
        this.listener = listener;
    }


    public void connect(String wsUrl, String authToken, String gameId) throws Exception {
        if (client != null && client.isRunning()) {
            disconnect();
        }
        client = new WebSocketClient();
        endpoint = new WebSocketClientEndpoint();
        client.start();
        endpoint.setMessageHandler(message -> {
            handleMessage(message);
        });
        URI uri = new URI(wsUrl);
        ClientUpgradeRequest request = new ClientUpgradeRequest();
        client.connect(endpoint, uri, request);
        UserGameCommand connectCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, Integer.parseInt(gameId));
    }

    @OnWebSocketMessage
    public void onMessage(String message) {
        System.out.println("Received: " + message);
        client.displayBoard();
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.println("Disconnected: " + reason);
        this.session = null;
    }

    public void sendMove(String moveJson) throws IOException {
        if (session != null && session.isOpen()) {
            session.getRemote().sendString("{\"commandType\":\"MAKE_MOVE\",\"authToken\":\"" + client.authToken + "\",\"gameID\":" + client.currentGameId + ",\"move\":" + moveJson + "}");
        }
    }

    public void sendResign() throws IOException {
        if (session != null && session.isOpen()) {
            session.getRemote().sendString("{\"commandType\":\"RESIGN\",\"authToken\":\"" + client.authToken + "\",\"gameID\":" + client.currentGameId + "}");
        }
    }

    public void sendLeave() throws IOException {
        if (session != null && session.isOpen()) {
            session.getRemote().sendString("{\"commandType\":\"LEAVE\",\"authToken\":\"" + client.authToken + "\",\"gameID\":" + client.currentGameId + "}");
        }



    }
}
