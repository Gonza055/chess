package server;

import dataaccess.DataAccessException;
import dataaccess.DataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.*;
import com.google.gson.Gson;
import spark.*;
import service.Results.*;
import dataaccess.MySQLDataAccess;
import websocket.WebSocketServer;

public class Server {

    private final DataAccess dataaccess;
    private final UserService userService;
    private final GameService gameService;
    private final SessionService sessionService;
    private final ClearService clearService;
    private final Gson gson;
    private final WebSocketServer webSocketServer;

    public Server() {
        try {
            this.dataaccess = new MySQLDataAccess();
            this.userService = new UserService(dataaccess);
            this.gameService = new GameService(dataaccess);
            this.sessionService = new SessionService(dataaccess);
            this.clearService = new ClearService(dataaccess);
            this.webSocketServer = new WebSocketServer(gameService);
            this.gson = new Gson();
        } catch (DataAccessException e) {
            throw new RuntimeException("Unable to connect to database", e);
        }
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        registerEndpoints();

        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        webSocketServer.start(8081);

        Spark.awaitInitialization();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                clearService.clear();
            } catch (Exception e) {
                System.err.println("Error clearing database: " + e.getMessage());
            }
        }));
        return Spark.port();
    }

    private void registerEndpoints() {
        registerUserEndpoints();
        registerSessionEndpoints();
        registerGameEndpoints();
        registerDbEndpoints();
    }

    private void registerUserEndpoints() {
        Spark.post("/user", (req, res) -> {
            try {
                RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);
                if (request == null || request.username == null || request.password == null || request.email == null) {
                    res.status(400);
                    return gson.toJson(new ErrorResponse("Error: Bad request"));
                }
                RegisterResult result = userService.register(request.username, request.password, request.email);
                res.status(200);
                return gson.toJson(result);
            } catch (DataAccessException e) {
                if (e.getMessage().equals("Username already exists")) {
                    res.status(403);
                    return gson.toJson(new ErrorResponse("Error: Already exists"));
                }
                res.status(500);
                return gson.toJson(new ErrorResponse("Error: " + e.getMessage()));
            }
        });
        Spark.post("/session", (req, res) -> {
            try {
                LoginRequest request = gson.fromJson(req.body(), LoginRequest.class);
                if (request == null || request.username == null || request.password == null) {
                    res.status(400);
                    return gson.toJson(new ErrorResponse("Error: Bad Request"));
                }
                RegisterResult result = userService.login(request.username, request.password);
                res.status(200);
                return gson.toJson(result);
            } catch (DataAccessException e) {
                if (e.getMessage().equals("Invalid Credentials")) {
                    res.status(401);
                    return gson.toJson(new ErrorResponse("Error: Unauthorized"));
                }
                res.status(500);
                return gson.toJson(new ErrorResponse("Error: " + e.getMessage()));
            }
        });
    }

    private void registerSessionEndpoints() {
        Spark.delete("/session", (req, res) -> {
            String authToken = req.headers("Authorization");
            if (authToken == null) {
                res.status(401);
                return gson.toJson(new ErrorResponse("Error: Unauthorized"));
            }
            try {
                if (dataaccess.getAuth(authToken) == null) {
                    res.status(401);
                    return gson.toJson(new ErrorResponse("Error: Unauthorized"));
                }
                sessionService.logout(authToken);

                res.status(200);
                return gson.toJson(new MessageResponse("user logged out"));
            } catch (DataAccessException e) {
                res.status(500);
                return gson.toJson(new ErrorResponse("Error: Bad Request"));
            }
        });

    }

    private void registerGameEndpoints() {
        Spark.get("/game", (req, res) -> {
            String authToken = req.headers("Authorization");
            if (authToken == null) {
                res.status(401);
                return gson.toJson(new ErrorResponse("Error: Unauthorized"));
            }
            try {
                GameListResult result = gameService.listGames(authToken);
                res.status(200);
                return gson.toJson(new GameListResponse(result.games()));
            } catch (DataAccessException e) {
                return handleDataAccessException(e, res);
            }
        });
        Spark.post("/game", (req, res) -> {
            String authToken = req.headers("Authorization");
            if (authToken == null) {
                res.status(401);
                return gson.toJson(new ErrorResponse("Error: Unauthorized"));
            }
            try {
                AuthData auth = dataaccess.getAuth(authToken);
                if (auth == null) {
                    res.status(401);
                    return gson.toJson(new ErrorResponse("Error: Unauthorized"));
                }
                UserData user = dataaccess.getUser(auth.username());
                if (user == null) {
                    res.status(401);
                    return gson.toJson(new ErrorResponse("Error: Unauthorized"));
                }
                if (req.body() == null || req.body().trim().isEmpty()) {
                    res.status(401);
                    return gson.toJson(new ErrorResponse("Error: Unauthorized"));
                }
                CreateGameRequest request = gson.fromJson(req.body(), CreateGameRequest.class);
                if (request == null || request.gameName == null) {
                    res.status(400);
                    return gson.toJson(new ErrorResponse("Error: Bad request"));
                }
                CreateGameResult result = gameService.createGame(authToken, request.gameName);
                res.status(200);
                return gson.toJson(result);
            } catch (DataAccessException e) {
                return handleDataAccessException(e, res);
            } catch (Exception e) {
                res.status(500);
                return gson.toJson(new ErrorResponse("Error: Bad request"));
            }
        });
        Spark.put("/game", (req, res) -> {
            String authToken = req.headers("Authorization");
            if (authToken == null) {
                res.status(401);
                return gson.toJson(new ErrorResponse("Error: Unauthorized"));
            }
            try {
                JoinGameRequest request = gson.fromJson(req.body(), JoinGameRequest.class);
                if (request == null || request.gameID == null || request.playerColor == null) {
                    res.status(400);
                    return gson.toJson(new ErrorResponse("Error: Bad request"));
                }
                JoinGameResult result = gameService.joinGame(authToken, request.gameID, request.playerColor);
                res.status(200);
                return gson.toJson(new EmptyResponse());
            } catch (DataAccessException e) {
                if (e.getMessage().equals("Unauthorized")) {
                    res.status(401);
                    return gson.toJson(new ErrorResponse("Error: Unauthorized"));
                } else if (e.getMessage().equals("Invalid Game") || e.getMessage().equals("Invalid Color")) {
                    res.status(400);
                    return gson.toJson(new ErrorResponse("Error: Bad request"));
                } else if (e.getMessage().equals("Player already joined")) {
                    res.status(403);
                    return gson.toJson(new ErrorResponse("Error: Player already joined"));
                }
                res.status(500);
                return gson.toJson(new ErrorResponse("Error: " + e.getMessage()));
            }
        });
    }

    private void registerDbEndpoints() {
        Spark.delete("/db", (req, res) -> {
            try {
                Result result = clearService.clear();
                res.status(200);
                return gson.toJson(new EmptyResponse());
            } catch (DataAccessException e) {
                res.status(500);
                return gson.toJson(new ErrorResponse("Error: " + e.getMessage()));
            }
        });
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
        webSocketServer.stop();
    }

    private Object handleDataAccessException(DataAccessException e, Response res) {
        if ("Unauthorized".equals(e.getMessage())) {
            res.status(401);
            return gson.toJson(new ErrorResponse("Error: Unauthorized"));
        }
        res.status(500);
        return gson.toJson(new ErrorResponse("Error: " + e.getMessage()));
    }

    private record RegisterRequest(String username, String password, String email) {}

    private record LoginRequest(String username, String password) {}

    private record CreateGameRequest(String gameName) {}

    private record JoinGameRequest(Integer gameID, String playerColor) {}

    private record GameListResponse(GameData[] games) {}

    private record ErrorResponse(String message) {}

    private record EmptyResponse() {}

    private record MessageResponse(String message) {}
}
