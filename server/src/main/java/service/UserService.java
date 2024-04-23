package service;
import dataAccess.UserDAO;
import request.LoginRequest;
import request.RegisterRequest;
import dataAccess.AuthDAO;
import response.LoginResponse;
import model.AuthData;
import model.UserData;
import response.LogoutResponse;
import java.util.UUID;
import response.RegisterResponse;
import dataAccess.DataAccessException;
public class UserService {
    public RegisterResponse regRespond(RegisterRequest req, UserDAO userO, AuthDAO authO) throws DataAccessException {
        String username = null;
        String authToken = null;
        String message = "";
        int status = 200;
        if (req.getUsername() == null || req.getPassword() == null || req.getEmail() == null) {
            username = null;
            authToken = null;
            message = "ERROR - Bad request";
            status = 400;
            return new RegisterResponse(username, authToken, message, status);
        }
        for (int i=0; i < userO.sizeGet(); i = i + 1) {
            if(userO.userGet(i) == null) continue;
            if (userO.userGet(i).username().equals(req.getUsername())) {
                message = "ERROR - User already exists";
                status = 403;
                return new RegisterResponse(null, null, message, status);
            }
        }
        UserData userDataToAdd = new UserData(req.getUsername(), req.getPassword(), req.getEmail());
        username = req.getUsername();
        userO.crtUser(userDataToAdd);
        AuthData authDataToAdd = new AuthData(UUID.randomUUID().toString(), req.getUsername());
        authToken = authDataToAdd.authToken();
        authO.crtAuth(authDataToAdd);
        return new RegisterResponse(username, authToken, message, status);
    }
    public LoginResponse loginRespond(LoginRequest req, UserDAO userObj, AuthDAO authObj) throws DataAccessException {
        String uname= req.getUsername();
        String authToken = "";
        for (int i=0; i < userObj.sizeGet(); i = i + 1) {
            if (userObj.userGet(i) == null) continue;
            if (userObj.userGet(i).username().equals(req.getUsername()) && req.password.equals(userObj.userGet(i).password())) {
                authToken = UUID.randomUUID().toString();
                authObj.crtAuth(new AuthData(authToken, uname));
                return new LoginResponse(uname, authToken, "", 200);
            }
        }
        return new LoginResponse(null, null, "ERROR - User does not exist", 401);
    }
    public LogoutResponse logoutRespond(String authTok, AuthDAO authO) throws DataAccessException {
        for (int i=0; i < authO.sizeGet(); i = i + 1) {
            if (authO.getAuthWID(i) == null) continue;
            if (authO.getAuthWID(i).authToken().equals(authTok)) {
                authO.rmvAuth(authO.getAuthWID(i));
                return new LogoutResponse(null, 200);
            }
        }
        return new LogoutResponse("ERROR - Unauthorized", 401);
    }
}