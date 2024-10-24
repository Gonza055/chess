package server;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthRecord;
import model.GameRecord;
import model.LoginRequest;
import model.UserRecord;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class Server {

    private final AuthService authService;
    private final GameService gameService;
    private final UserService userService;
    private final Gson gson;

    public Server() {
        this.authService = new AuthService();
        this.gameService = new GameService();
        this.userService = new UserService();
        this.gson = new Gson();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        Map<String, BiFunction<Request, Response, Object>> routes = new HashMap<>();
        routes.put("/game", this::listGames);
        routes.put("/game/join", this::joinGame);
        routes.put("/game/create", this::createGame);
        routes.put("/user/register", this::registerUser);
        routes.put("/session/login", this::loginUser);
        routes.put("/session/logout", this::logoutUser);
        routes.put("/db/clear", this::deleteAll);

        routes.forEach((path, handler) -> Spark.post(path, (req, res) -> handleRequest(req, res, handler)));

        Spark.exception(ResponseException.class, this::exceptionHandler);

        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object handleRequest(Request req, Response res, BiFunction<Request, Response, Object> handler) {
        try {
            return handler.apply(req, res);
        } catch (ResponseException e) {
            res.status(e.getStatusCode());
            return gson.toJson(Map.of("error", e.getMessage()));
        }
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.getStatusCode());
        res.body(gson.toJson(Map.of("error", ex.getMessage())));
    }
}
