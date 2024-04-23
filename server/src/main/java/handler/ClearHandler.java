package handler;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import service.DBService;
import dataAccess.AuthDAO;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
public class ClearHandler {
    public Object handle(Request req, Response res, UserDAO userO, AuthDAO authO, GameDAO gameO) throws DataAccessException {
        Gson myGsn = new Gson();
        DBService myservice= new DBService();
        return myGsn.toJson(myservice.clearRespond(userO, authO, gameO));
    }
}
