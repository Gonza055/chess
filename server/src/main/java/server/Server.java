package server;
import spark.*;
import websocket.WebSocketHandler;
import dataAccess.*;
import handler.*;
public class Server {
    public UserDAO userObj = new SQLUserDAO();
    public AuthDAO authObj =new SQLAuthDAO() {
        @Override
        public void clrAuthList() {
        }
    };
    public GameDAO gameObj = new SQLGameDAO();
    public static void main(String[] args) {
        Server theServer= new Server();
        theServer.run(Integer.parseInt(args[0]));
    }
    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");
        Spark.webSocket("/connect", WebSocketHandler.class);
        try {
            DatabaseManager.createDatabase();
            userObj.updIndex();
            authObj.updIndex();
            gameObj.updIndex();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Spark.delete("/db", ((request, response) -> new ClearHandler().handle(request, response, userObj, authObj, gameObj)));
        Spark.post("/user", ((request, response) -> new RegisterHandler().handle(request, response, userObj, authObj)));
        Spark.post("/session", ((request, response) -> new LoginHandler().handle(request, response, userObj, authObj)));
        Spark.delete("/session", ((request, response) -> new LogoutHandler().handle(request, response, authObj)));
        Spark.get("/game", ((request, response) -> new ListGamesHandler().handle(request, response, authObj, gameObj)));
        Spark.post("/game", ((request, response) -> new CreateGameHandler().handle(request, response, authObj, gameObj)));
        Spark.put("/game", ((request, response) -> new JoinGameHandler().handle(request, response, authObj, gameObj)));
        Spark.awaitInitialization();
        return Spark.port();
    }
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}