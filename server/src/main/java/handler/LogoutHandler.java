package handler;
import dataAccess.DataAccessException;
import response.LogoutResponse;
import dataAccess.AuthDAO;
import spark.Response;
import service.UserService;
import com.google.gson.Gson;
import spark.Request;
public class LogoutHandler {
    public Object handle(Request req, Response res, AuthDAO authO) throws DataAccessException {
        Gson gson = new Gson();
        String authToken = req.headers("authorization");
        UserService myUserService = new UserService();
        LogoutResponse myLogoutResponse = myUserService.logoutRespond(authToken, authO);
        res.status(myLogoutResponse.status);
        return gson.toJson(myLogoutResponse);
    }
}
