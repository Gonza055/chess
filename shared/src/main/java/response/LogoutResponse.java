package response;
public class LogoutResponse {
    public String message;
    public int status;
    public LogoutResponse(String msg, int stat) {
        this.message = msg;
        this.status = stat;
    }
}
