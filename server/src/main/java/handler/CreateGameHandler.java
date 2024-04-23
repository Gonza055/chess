package handler;
import dataAccess.DataAccessException;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import spark.Request;
import response.CreateGameResponse;
import service.GameService;
import spark.Response;
import dataAccess.GameDAO;
import request.CreateGameRequest;
public class CreateGameHandler {
    public Object handle(Request req, Response res, AuthDAO authO, GameDAO gameO) throws DataAccessException {
        Gson mygson= new Gson();
        CreateGameRequest myrequest= mygson.fromJson(req.body(), CreateGameRequest.class);
        String authTok = req.headers("authorization");
        GameService myGameService = new GameService();
        CreateGameResponse myCrteGameResponse = myGameService.createGameRespond(myrequest, authTok, authO, gameO);
        res.status(myCrteGameResponse.status);
        return mygson.toJson(myCrteGameResponse);
    }
}
