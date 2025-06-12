package service;
import dataaccess.DataAccessException;
import dataaccess.DataAccess;
import model.*;

import service.Results.*;
import chess.*;

import java.util.Objects;

public class GameService {

    private final DataAccess dataaccess;
    private final java.util.Map<Integer, ChessGame> activeGames = new java.util.HashMap<>();
    private final com.google.gson.Gson gson = new com.google.gson.Gson();

    public GameService(DataAccess dataaccess) {
        this.dataaccess = dataaccess;
    }

    private boolean isValidAuthToken(String authToken) throws DataAccessException {
        return dataaccess.getAuth(authToken) != null;
    }

    public String getUsernameFromAuth(String authToken) throws DataAccessException {
        AuthData authData = dataaccess.getAuth(authToken);
        if (authData == null) {
            throw new DataAccessException("Unauthorized: Invalid auth token.");
        }
        return authData.username();
    }

    public GameListResult listGames(String authToken) throws DataAccessException {
        if (!isValidAuthToken(authToken)) {
            throw new DataAccessException("Unauthorized");
        }
        return new GameListResult(dataaccess.getAllGames());
    }

    public CreateGameResult createGame(String authToken, String gameName) throws DataAccessException {
        if (!isValidAuthToken(authToken)) {
            throw new DataAccessException("Unauthorized");
        }
        String username = getUsernameFromAuth(authToken);

        int gameID = dataaccess.generateGameID();
        ChessGame newGame = new ChessGame();
        newGame.getBoard().resetBoard();
        GameData game = new GameData(gameID, null, null, gameName, newGame);
        dataaccess.createGame(game);

        activeGames.put(gameID, newGame);

        return new CreateGameResult(gameID);
    }

    public JoinGameResult joinGame(String authToken, int gameID, String playerColor) throws DataAccessException {
        if (!isValidAuthToken(authToken)) {
            throw new DataAccessException("Unauthorized");
        }
        String username = getUsernameFromAuth(authToken);

        GameData gameData = dataaccess.getGame(gameID);
        if (gameData == null) {
            throw new DataAccessException("Bad game ID: Invalid game");
        }

        ChessGame currentChessGame = activeGames.get(gameID);
        if (currentChessGame == null) {
            currentChessGame = gameData.game();
            if (currentChessGame == null) {
                currentChessGame = new ChessGame();
                currentChessGame.getBoard().resetBoard();
            }
            activeGames.put(gameID, currentChessGame);
        }

        String whiteUsername = gameData.whiteUsername();
        String blackUsername = gameData.blackUsername();

        if ("white".equalsIgnoreCase(playerColor)) {
            if (whiteUsername != null && !whiteUsername.equals(username)) {
                throw new DataAccessException("Already taken");
            }
            whiteUsername = username;
        } else if ("black".equalsIgnoreCase(playerColor)) {
            if (blackUsername != null && !blackUsername.equals(username)) {
                throw new DataAccessException("Already taken");
            }
            blackUsername = username;
        } else if (!"observer".equalsIgnoreCase(playerColor)) {
            throw new DataAccessException("Bad Request: Invalid Color");
        }

        if (Objects.equals(gameData.whiteUsername(), username) && "white".equalsIgnoreCase(playerColor)) {
        } else if (Objects.equals(gameData.blackUsername(), username) && "black".equalsIgnoreCase(playerColor)) {
        } else if (dataaccess.isObserver(gameID, username) && "observer".equalsIgnoreCase(playerColor)) {
        } else {
            GameData updatedGameData = new GameData(
                    gameID,
                    whiteUsername,
                    blackUsername,
                    gameData.gameName(),
                    currentChessGame
            );
            dataaccess.updateGame(gameID, updatedGameData);
        }

        return new JoinGameResult(authToken, gameID, playerColor);
    }

    public String getGameState(int gameId, String authToken) throws DataAccessException {
        if (!isValidAuthToken(authToken)) {
            throw new DataAccessException("Unauthorized");
        }
        GameData game = dataaccess.getGame(gameId);
        if (game == null || !activeGames.containsKey(gameId)) {
            throw new DataAccessException("Invalid game");
        }
        ChessGame chessGame = activeGames.get(gameId);
        return gson.toJson(chessGame.getBoard());
    }

    public void resign(int gameId, String authToken) throws DataAccessException {
        if (!isValidAuthToken(authToken)) {
            throw new DataAccessException("Unauthorized");
        }
        ChessGame game = activeGames.get(gameId);
        if (game == null) {
            throw new DataAccessException("Invalid game");
        }
        //game.setGameOver(true);
        GameData gameData = dataaccess.getGame(gameId);
        dataaccess.updateGame(gameId, new GameData(gameId, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game));
    }
}