package serviceTests;
import dataAccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.*;
import service.DBService;
import service.GameService;
import service.UserService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
public class APIServiceTests {

    private UserService userService = new UserService();
    private DBService dbService = new DBService();
    private GameService gameService = new GameService();
    private UserDAO userDAO = new MemoryUserDAO();
    private AuthDAO authDAO = new MemoryAuthDAO();
    private GameDAO gameDAO = new MemoryGameDAO();
    @BeforeEach
    public void setup() {
        userDAO.clrUList();
        authDAO.clearAuthList();
        gameDAO.clearGList();
    }
    @Test
    public void goodRegRespond() throws DataAccessException {
        RegisterRequest req = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse testResp = userService.regRespond(req, userDAO, authDAO);
        assertEquals(authDAO.getAuthWID(0).username(), req.username);
        assertEquals(userDAO.userGet(0).username(), req.username);
    }
    @Test
    public void badRegRespond() throws DataAccessException {
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterRequest badReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse testResp = userService.regRespond(badReq, userDAO, authDAO);
        assertNotEquals(authDAO.getAuthWID(0).username(), null);
        assertNotEquals(userDAO.userGet(0).username(), null);
    }
    @Test
    public void goodLoginRespond() throws DataAccessException {
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        LoginRequest req = new LoginRequest("TestUser", "Pass");
        LoginResponse testResp = userService.loginRespond(req, userDAO, authDAO);
        assertEquals(userDAO.userGet(0).password(), req.password);
    }
    @Test
    public void badLoginRequest() throws DataAccessException {
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        LoginRequest badReq = new LoginRequest("BadUser", "Pass");
        LoginResponse testResp = userService.loginRespond(badReq, userDAO, authDAO);
        assertNotEquals(null, userDAO.userGet(0).password());
    }
    @Test
    public void goodLogoutRequest() throws DataAccessException {
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        LogoutResponse testResp = userService.logoutRespond(authDAO.getAuthWID(0).authToken(), authDAO);
        assertEquals(authDAO.sizeGet(), 0);
    }
    @Test
    public void badLogoutRequest() throws DataAccessException {
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        LogoutResponse testResp = userService.logoutRespond("auth", authDAO);
        assertNotEquals(authDAO.sizeGet(), 0);
    }
    @Test
    public void goodClearRequest() throws DataAccessException {
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        ClearResponse testResp = dbService.clearRespond(userDAO, authDAO, gameDAO);
        assertEquals(userDAO.sizeGet(), 0);
        assertEquals(authDAO.sizeGet(), 0);
        assertEquals(gameDAO.sizeGet(), 0);
    }
    @Test
    public void badClearRequest() throws DataAccessException {
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        ClearResponse testResp = dbService.clearRespond(userDAO, authDAO, gameDAO);
        assertNotEquals(userDAO.sizeGet(), 1);
        assertNotEquals(authDAO.sizeGet(), 1);
        assertNotEquals(gameDAO.sizeGet(), 1);
    }
    @Test
    public void goodCreateGameRequest() throws DataAccessException {
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        CreateGameRequest req = new CreateGameRequest("Game");
        CreateGameResponse resp = gameService.createGameRespond(req, authDAO.getAuthWID(0).authToken(), authDAO, gameDAO);
        assertEquals(gameDAO.gameGet(0).gameName(), req.gameName);
    }
    @Test
    public void badCreateGameRequest() throws DataAccessException {
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        CreateGameRequest req = new CreateGameRequest("Game");
        CreateGameResponse resp = gameService.createGameRespond(req, "BadAuthToken", authDAO, gameDAO);
        assertNotEquals(req.gameName, gameDAO.sizeGet());
    }
    @Test
    public void goodListGamesRequest() throws DataAccessException {
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        CreateGameRequest createReq = new CreateGameRequest("Game");
        CreateGameResponse createResp = gameService.createGameRespond(createReq, authDAO.getAuthWID(0).authToken(), authDAO, gameDAO);
        ListGamesResponse resp = gameService.listGamesRespond(authDAO.getAuthWID(0).authToken(), authDAO, gameDAO);
        assertEquals(gameDAO.sizeGet(), 1);
    }
    @Test
    public void badListGamesRequest() throws DataAccessException {
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        CreateGameRequest createReq = new CreateGameRequest("Game");
        CreateGameResponse createResp = gameService.createGameRespond(createReq, authDAO.getAuthWID(0).authToken(), authDAO, gameDAO);
        ListGamesResponse resp = gameService.listGamesRespond("BadAuthToken", authDAO, gameDAO);
        assertNotEquals(resp.games, gameDAO.returnGameList());
    }
    @Test
    public void goodJoinGameRequest() throws DataAccessException {
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        CreateGameRequest createReq = new CreateGameRequest("Game");
        CreateGameResponse createResp = gameService.createGameRespond(createReq, authDAO.getAuthWID(0).authToken(), authDAO, gameDAO);
        JoinGameRequest req = new JoinGameRequest("WHITE", gameDAO.gameGet(0).gameID());
        JoinGameResponse resp = gameService.joinGameRespond(req, authDAO.getAuthWID(0).authToken(), authDAO, gameDAO);
        assertEquals(200, resp.status);
    }
    @Test
    public void badJoinGameRequest() throws DataAccessException {
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        CreateGameRequest createReq = new CreateGameRequest("Game");
        CreateGameResponse createResp = gameService.createGameRespond(createReq, authDAO.getAuthWID(0).authToken(), authDAO, gameDAO);
        JoinGameRequest req = new JoinGameRequest("WHITE", gameDAO.gameGet(0).gameID());
        JoinGameResponse resp = gameService.joinGameRespond(req, "BadAuthToken", authDAO, gameDAO);
        assertNotEquals(200, resp.status);
    }
}