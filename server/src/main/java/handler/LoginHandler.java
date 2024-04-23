package handler;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import service.UserService;
import spark.Request;
import spark.Response;
import request.LoginRequest;
import response.LoginResponse;
public class LoginHandler {
    public Object handle(Request req, Response res, UserDAO userO, AuthDAO authO) throws DataAccessException {
        Gson mygson = new Gson();
        LoginRequest myRequest = mygson.fromJson(req.body(), LoginRequest.class);
        UserService myUserService = new UserService();
        LoginResponse myLoginResponse = myUserService.loginRespond(myRequest, userO, authO);
        res.status(myLoginResponse.status);
        return mygson.toJson(myLoginResponse);
    }
}
