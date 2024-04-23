package handler;
import spark.Request;
import spark.Response;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import response.ListGamesResponse;
import service.GameService;
public class ListGamesHandler {
    public Object handle(Request req, Response res, AuthDAO authO, GameDAO gameO) throws DataAccessException {
        Gson myGson = new Gson();
        String authToken = req.headers("authorization");
        GameService service= new GameService();
        ListGamesResponse myListGamesResponse = service.listGamesRespond(authToken, authO, gameO);
        res.status(myListGamesResponse.status);
        return myGson.toJson(myListGamesResponse);
    }
}
