package webSocketMessages.userCommands;
import java.util.Objects;
public class UserGameCommand {
    public UserGameCommand(String authToken) {
        this.authToken = authToken;
    }
    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }
    protected CommandType commandType;
    private final String authToken;
    public String getAuthString() {
        return authToken;
    }
    public CommandType getCommandType() {
        return this.commandType;
    }
    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthString());
    }
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserGameCommand))
            return false;
        UserGameCommand tht = (UserGameCommand) o;
        return getCommandType() == tht.getCommandType() && Objects.equals(getAuthString(), tht.getAuthString());
    }

}
