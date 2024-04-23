package websocket;
import webSocketMessages.serverMessages.ServerMessage;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import java.io.IOException;
public class Connection {
    public String authToken;
    public Session session;
    public Integer gameID;
    public Connection(String authTok, Session sess, int gID) {
        this.authToken = authTok;
        this.session = sess;
        this.gameID = gID;
    }
    public void send(ServerMessage msg) throws IOException {
        try {
            session.getRemote().sendString(new Gson().toJson(msg));
        }
        catch (IOException e) {
            System.out.println("Error: Failed to send message");
        }
    }
}