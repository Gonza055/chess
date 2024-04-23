package webSocketMessages.serverMessages;
public class Error extends ServerMessage{
    String errorMessage;
    public Error(ServerMessageType type, String errMessage) {
        super(type);
        this.errorMessage = errMessage;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
}
