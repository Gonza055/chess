package server;

import chess.ChessGame;
import com.google.gson.Gson;

import model.AuthRecord;
import model.GameRecord;
import model.LoginRequest;
import model.UserRecord;
import spark.*;

import logic.Service;
import logic.Exception;

import java.util.List;
import java.util.Map;

public class Server {

    private final Service service;

    public Server() {
        this.service = new Service();
    }

    public int run(int desiredPort) {
        configureServer(desiredPort);
        registerRoutes();
        setupExceptionHandling();

        Spark.init();
        Spark.awaitInitialization();

        return Spark.port();
    }

    private void configureServer(int port) {
        Spark.port(port);
        Spark.staticFiles.location("web");
    }

    private void registerRoutes() {
        Spark.get("/game", (req, res) -> listGames(req, res));
        Spark.put("/game", (req, res) -> joinGame(req, res));
        Spark.post("/game", (req, res) -> createGame(req, res));
        Spark.post("/user", (req, res) -> registerUser(req, res));
        Spark.post("/session", (req, res) -> loginUser(req, res));
        Spark.delete("/session", (req, res) -> logoutUser(req, res));
        Spark.delete("/db", (req, res) -> deleteAll(req, res));
    }

    private void setupExceptionHandling() {
        Spark.exception(Exception.class, (ex, req, res) -> handleException(ex, req, res));
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object joinGame(Request req, Response res) throws Exception {
        String authToken = req.headers("authorization");
        JoinRequest joinRequest = new Gson().fromJson(req.body(), JoinRequest.class);
        service.joinGame(authToken, joinRequest.playerColor(), joinRequest.gameID());
        return "{}";
    }

    private Object loginUser(Request req, Response res) throws Exception {
        LoginRequest loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);
        AuthRecord authRecord = service.login(loginRequest);
        return new Gson().toJson(authRecord);
    }

    private Object createGame(Request req, Response res) throws Exception {
        String authToken = req.headers("authorization");
        GameName gameRequest = new Gson().fromJson(req.body(), GameName.class);
        int gameId = service.newGame(authToken, gameRequest.gameName());
        return new Gson().toJson(Map.of("gameID", gameId));
    }

    private Object registerUser(Request req, Response res) throws Exception {
        UserRecord userRequest = new Gson().fromJson(req.body(), UserRecord.class);
        AuthRecord authRecord = service.register(userRequest);
        return new Gson().toJson(authRecord);
    }

    private Object logoutUser(Request req, Response res) throws Exception {
        String authToken = req.headers("authorization");
        service.logout(authToken);
        return "{}";
    }

    private Object listGames(Request req, Response res) throws Exception {
        String authToken = req.headers("authorization");
        List<GameRecord> games = service.listGames(authToken);
        return new Gson().toJson(Map.of("games", games));
    }

    private Object deleteAll(Request req, Response res) {
        res.status(200);
        service.deleteDB();
        return "{}";
    }

    private void handleException(Exception ex, Request req, Response res) {
        res.status(ex.statusCode());
        res.body(new Gson().toJson(new ErrorMessage(ex.getMessage())));
    }

    private record ErrorMessage(String message) {}

    private record JoinRequest(ChessGame.TeamColor playerColor, int gameID) {}

    private record GameName(String gameName) {}
}
