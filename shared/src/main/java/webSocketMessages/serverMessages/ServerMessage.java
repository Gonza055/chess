package webSocketMessages.serverMessages;
import java.util.Objects;
public class ServerMessage {
    ServerMessageType serverMessageType;
    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }
    public ServerMessage(ServerMessageType typ) {
        this.serverMessageType = typ;
    }
    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }
    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ServerMessage))
            return false;
        ServerMessage tht = (ServerMessage) o;
        return getServerMessageType() == tht.getServerMessageType();
    }
}
