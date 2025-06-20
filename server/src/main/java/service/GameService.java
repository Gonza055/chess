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
            throw new DataAccessException("Unauthorized: Invalid auth token");
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

    public ChessGame getGameState(int gameId, String authToken) throws DataAccessException {
        authorize(authToken);
        GameData gameData = getGameData(gameId);
        return ensureGameLoaded(gameId, gameData);
    }

    public void makeMove(int gameId, String authToken, ChessMove move) throws DataAccessException, InvalidMoveException {
        String username = authorize(authToken);
        GameData gameData = getGameData(gameId);
        ChessGame chessGame = ensureGameLoaded(gameId, gameData);

        if (chessGame.isGameOver()) {
            throw new DataAccessException("Bad Request: Cannot make move: Game is already over");
        }

        ChessGame.TeamColor playerColor = getPlayerColor(gameData, username);
        if (chessGame.getTeamTurn() != playerColor) {
            throw new DataAccessException("Bad Request: Not your turn!");
        }

        chessGame.makeMove(move);

        dataaccess.updateGame(gameId, new GameData(
                gameId,
                gameData.whiteUsername(),
                gameData.blackUsername(),
                gameData.gameName(),
                chessGame));
    }

    public void resign(int gameId, String authToken) throws DataAccessException {
        String username = authorize(authToken);
        GameData gameData = getGameData(gameId);
        ChessGame chessGame = ensureGameLoaded(gameId, gameData);

        if (!isPlayerInGame(gameData, username)) {
            throw new DataAccessException("Forbidden: Only players can resign from a game");
        }

        if (chessGame.isGameOver()) {
            throw new DataAccessException("Bad Request: Cannot resign: Game is already over");
        }

        chessGame.setGameOver(true);
        updateGameState(gameId, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), chessGame);
    }


    public void leaveGame(int gameId, String authToken) throws DataAccessException {
        String username = authorize(authToken);
        GameData gameData = getGameData(gameId);
        ChessGame chessGame = ensureGameLoaded(gameId, gameData);

        String whiteUsername = gameData.whiteUsername();
        String blackUsername = gameData.blackUsername();

        boolean wasPlayer = false;
        if (Objects.equals(whiteUsername, username)) {
            whiteUsername = null;
            wasPlayer = true;
        }
        if (Objects.equals(blackUsername, username)) {
            blackUsername = null;
            wasPlayer = true;
        }

        if (!wasPlayer && !dataaccess.isObserver(gameId, username)) {
            throw new DataAccessException("Bad Request: User is not in this game");
        }

        updateGameState(gameId, whiteUsername, blackUsername, gameData.gameName(), chessGame);
    }

    private String authorize(String authToken) throws DataAccessException {
        if (!isValidAuthToken(authToken)) {
            throw new DataAccessException("Unauthorized");
        }
        return getUsernameFromAuth(authToken);
    }

    private GameData getGameData(int gameId) throws DataAccessException {
        GameData gameData = dataaccess.getGame(gameId);
        if (gameData == null) {
            throw new DataAccessException("Bad game ID: Invalid game");
        }
        return gameData;
    }

    private ChessGame ensureGameLoaded(int gameId, GameData gameData) throws DataAccessException {
        ChessGame game = activeGames.get(gameId);
        if (game == null) {
            game = gameData.game();
            if (game == null) {
                throw new DataAccessException("Game state is corrupted");
            }
            activeGames.put(gameId, game);
        }
        return game;
    }

    private ChessGame.TeamColor getPlayerColor(GameData gameData, String username) throws DataAccessException {
        if (username.equals(gameData.whiteUsername())) {
            return ChessGame.TeamColor.WHITE;
        } else if (username.equals(gameData.blackUsername())) {
            return ChessGame.TeamColor.BLACK;
        }
        throw new DataAccessException("Unauthorized: Only players can make moves");
    }

    private boolean isPlayerInGame(GameData gameData, String username) {
        return Objects.equals(gameData.whiteUsername(), username) || Objects.equals(gameData.blackUsername(), username);
    }

    private void updateGameState(int gameId,
                                 String whiteUsername,
                                 String blackUsername,
                                 String gameName,
                                 ChessGame chessGame) throws DataAccessException {
        GameData updatedGameData = new GameData(gameId, whiteUsername, blackUsername, gameName, chessGame);
        dataaccess.updateGame(gameId, updatedGameData);
    }


    public void clear() throws DataAccessException {
        dataaccess.clear();
        activeGames.clear();
    }
}