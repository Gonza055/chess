package service;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import dataAccess.GameDAO;
import dataAccess.AuthDAO;
import response.ClearResponse;
;public class DBService {
    public ClearResponse clearRespond(UserDAO userO, AuthDAO authO, GameDAO gameO) throws DataAccessException {
        userO.clrUList();
        authO.clearAuthList();
        gameO.clearGList();
        ClearResponse fres = new ClearResponse(null);
        return fres;
    }
}
