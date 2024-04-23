package service;
import chess.ChessGame;
import model.GameData;
import dataAccess.GameDAO;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.JoinGameResponse;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import response.CreateGameResponse;
import response.ListGamesResponse;
public class GameService {
    public ListGamesResponse listGamesRespond(String authTok, AuthDAO authO, GameDAO gameO) throws DataAccessException {
        boolean auth= false;
        for (int i=0; i < authO.sizeGet(); i = i + 1) {
            if (authO.getAuthWID(i) == null) continue;
            if (authTok.equals(authO.getAuthWID(i).authToken())) {
                auth= true;
                break;
            }
        }
        if (auth) {
            return new ListGamesResponse(gameO.returnGameList(), "", 200);
        } else {
            return new ListGamesResponse(null, "ERROR - Unauthorized", 401);
        }
    }
    public CreateGameResponse createGameRespond(CreateGameRequest req, String authTok, AuthDAO authO, GameDAO gameO) throws DataAccessException {
        boolean auth = false;
        if (req.getGameName() == null) {
            return new CreateGameResponse(null, "ERROR - Bad request", 400);
        }
        for (int i=0; i < authO.sizeGet(); i = i + 1) {
            if (authO.getAuthWID(i) == null) continue;
            if (authO.getAuthWID(i).authToken().equals(authTok)) {
                auth = true;
                break;
            }
        }
        if (auth) {
            int newGameID = gameO.getCurrID();
            GameData gameDataToAdd = new GameData(newGameID, null, null, req.getGameName(), new ChessGame());
            gameO.crtGame(gameDataToAdd);
            return new CreateGameResponse(newGameID, null, 200);
        } else {
            return new CreateGameResponse(null, "ERROR - Unauthorized", 401);
        }
    }
    public JoinGameResponse joinGameRespond(JoinGameRequest req, String authTok, AuthDAO authO, GameDAO gameO) throws DataAccessException {
        boolean auth = false;
        int userNumber = 0;
        if (req.gameID == 0) {
            return new JoinGameResponse("ERROR - Bad request", 400, null);
        } else {
            for (int i=0; i < authO.sizeGet(); i = i + 1) {
                if (authO.getAuthWID(i) == null) continue;
                if (authO.getAuthWID(i).authToken().equals(authTok)) {
                    auth = true;
                    userNumber = i;
                    break;
                }
            }
        }
        if (auth) {
            for (int i=0; i < gameO.sizeGet(); i = i + 1) {
                if (gameO.gameGet(i) == null) continue;
                if (gameO.gameGet(i).gameID() == req.getGameID()) {
                    ChessGame chessGame = gameO.gameGet(i).game();
                    if (req.getPlayerColor() == null) {
                        return new JoinGameResponse(null, 200, chessGame);
                    } else {
                        if (req.getPlayerColor() != null && req.getPlayerColor().equals("WHITE") && gameO.gameGet(i).whiteUsername() == null) {
                            String newW= authO.getAuthWID(userNumber).username();
                            GameData gameUpdate= new GameData(req.getGameID(), newW, gameO.gameGet(i).blackUsername(), gameO.gameGet(i).gameName(), gameO.gameGet(i).game());
                            gameO.gameSet(i, gameUpdate);
                            return new JoinGameResponse("", 200, chessGame);
                        } else if (req.getPlayerColor() != null && req.getPlayerColor().equals("BLACK") && gameO.gameGet(i).blackUsername() == null) {
                            String newB= authO.getAuthWID(userNumber).username();
                            GameData gameToUpdate = new GameData(req.getGameID(), gameO.gameGet(i).whiteUsername(), newB, gameO.gameGet(i).gameName(), gameO.gameGet(i).game());
                            gameO.gameSet(i, gameToUpdate);
                            return new JoinGameResponse("", 200, chessGame);
                        } else {
                            return new JoinGameResponse("ERROR - Already taken", 403, null);
                        }
                    }
                }
            }
        }
        return new JoinGameResponse("ERROR - Unauthorized", 401, null);
    }
}