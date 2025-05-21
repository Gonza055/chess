package server;

import dataaccess.MemoryDataAccess;
import dataaccess.dataAccess;
import service.*;
import com.google.gson.Gson;
import spark.*;
import service.Results.*;

public class Server {

    private final dataAccess dataaccess;
    private final UserService userService;
    private final GameService gameService;
    private final SessionService sessionService;
    private final ClearService clearService;
    private final Gson gson;

    public Server() {
        this.dataaccess = new MemoryDataAccess();
        this.userService = new UserService(dataaccess);
        this.gameService = new GameService(dataaccess);
        this.sessionService = new SessionService(dataaccess);
        this.clearService = new ClearService(dataaccess);
        this.gson = new Gson();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
