package response;
public class RegisterResponse {
    public String username;
    public String authToken;
    public String message;
    public int status;
    public RegisterResponse(String username, String authToken, String message, int status) {
        this.username = username;
        this.authToken = authToken;
        this.message = message;
        this.status = status;
    }
}
