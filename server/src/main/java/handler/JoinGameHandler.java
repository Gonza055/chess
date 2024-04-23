package handler;
import dataAccess.GameDAO;
import request.JoinGameRequest;
import dataAccess.DataAccessException;
import response.JoinGameResponse;
import spark.Request;
import service.GameService;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import spark.Response;
public class JoinGameHandler {
    public Object handle(Request req, Response res, AuthDAO authO, GameDAO gameO) throws DataAccessException {
        Gson mygson= new Gson();
        JoinGameRequest myRequest = mygson.fromJson(req.body(), JoinGameRequest.class);
        String authToken = req.headers("authorization");
        GameService myGameService = new GameService();
        JoinGameResponse myJoinGameResponse = myGameService.joinGameRespond(myRequest, authToken, authO, gameO);
        res.status(myJoinGameResponse.status);
        return mygson.toJson(myJoinGameResponse);
    }
}
