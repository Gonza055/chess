package handler;
import spark.Request;
import response.RegisterResponse;
import com.google.gson.Gson;
import request.RegisterRequest;
import service.UserService;
import dataAccess.DataAccessException;
import dataAccess.AuthDAO;
import spark.Response;
import dataAccess.UserDAO;
public class RegisterHandler {
    public Object handle (Request req, Response res, UserDAO userO, AuthDAO authO) throws DataAccessException {
        Gson jayson= new Gson();
        RegisterRequest theRequest = jayson.fromJson(req.body(), RegisterRequest.class);
        UserService userService = new UserService();
        RegisterResponse regResponse = userService.regRespond(theRequest, userO, authO);
        res.status(regResponse.status);
        return jayson.toJson(regResponse);
    }
}
