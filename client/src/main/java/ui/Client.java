package ui;

import java.io.IOException;
import java.util.Scanner;
import chess.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import websocket.WebSocketClientManager;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.commands.ConnectCommand;
import websocket.messages.ServerMessage;
import websocket.messages.ServerMessageError;
import websocket.messages.ServerMessageNotification;
import websocket.messages.LoadGameMessage;

public class Client implements WebSocketClientManager.ClientMessageObserver {

    private final String serverURL;
    private final ServerFacade serverFacade;
    private WebSocketClientManager wsClient;
    public boolean isRunning;
    private boolean isLoggedIn;
    private ChessBoard currentBoard;
    private ChessGame.TeamColor currentPlayerColor;
    private String authToken;
    private Integer currentGameId;
    private boolean inGame;


    public Client(String serverURL) {
        this.serverURL = serverURL;
        this.serverFacade = new ServerFacade(serverURL);
        this.isRunning = true;
        this.isLoggedIn = false;
        this.currentBoard = new ChessBoard();
        this.currentBoard.resetBoard();
        this.currentPlayerColor = null;
        this.authToken = null;
        this.currentGameId = null;
        this.inGame = false;
    }

    public void run() {
        System.out.println("Welcome to 240 Chess! Type Help to get started");
        Scanner scanner = new Scanner(System.in);
        while(isRunning) {
            String command = scanner.nextLine().trim().toLowerCase();
            handleCommand(command, scanner);
        }
        scanner.close();
    }

    @Override
    public void onGameLoad(LoadGameMessage message) {
        System.out.println("\n--- ¡Juego Cargado! ---");
        this.currentBoard = message.getGame().getBoard();
        displayBoard(this.currentBoard, this.currentPlayerColor != null ? this.currentPlayerColor : ChessGame.TeamColor.WHITE);
        System.out.print("[IN GAME] Ingresa un comando (help, redraw, leave, make move, resign): ");
    }

    @Override
    public void onNotification(ServerMessageNotification message) {
        System.out.println("\n" + "--- Notificación del Servidor ---" + EscapeSequences.RESET_TEXT_COLOR);
        System.out.println(message.getMessage());
        printPrompt();
    }

    @Override
    public void onError(ServerMessageError message) {
        System.out.println("\n" + EscapeSequences.SET_TEXT_COLOR_RED + "--- ¡ERROR del Servidor! ---" + EscapeSequences.RESET_TEXT_COLOR);
        System.err.println(EscapeSequences.SET_TEXT_COLOR_RED + "Error: " + message.getErrorMessage() + EscapeSequences.RESET_TEXT_COLOR);
        printPrompt();
    }

    private void handleCommand(String command, Scanner scanner) {
        if (inGame) {
            handleGameCommand(command, scanner);
        } else if (isLoggedIn) {
            handlePostLoginCommand(command, scanner);
        } else {
            handlePreLoginCommand(command, scanner);
        }
    }

    private void handlePreLoginCommand(String command, Scanner scanner) {
        switch (command) {
            case "help":
                displayHelp();
                break;
            case "quit":
                isRunning = false;
                System.out.println("Goodbye!");
                break;
            case "login":
                handleLogin(scanner);
                break;
            case "register":
                handleRegister(scanner);
                break;
            default:
                System.out.println("Invalid command, type Help for a list of commands");
        }
    }

    private void handlePostLoginCommand(String command, Scanner scanner) {
        switch (command) {
            case "help":
                displayHelp();
                break;
            case "logout":
                handleLogout();
                break;
            case "create game":
                handleCreateGame(scanner);
                break;
            case "list games":
                handleListGames();
                break;
            case "join game":
                handleJoinGame(scanner);
            case "observe game":
                handleObserveGame(scanner);
                break;
            default:
                System.out.println("Invalid command, type Help for a list of commands");
        }
    }

    private void handleGameCommand(String command, Scanner scanner) {
        switch (command) {
            case "move":
                handleMove(scanner);
                break;
            case "resign":
                handleResign();
                break;
            case "leave":
                handleLeave();
                break;
            default:
                System.out.println("Invalid command, type Help for a list of commands");
        }
    }

    private void handleMove(Scanner scanner) {
        if (currentGameId == null) {
            System.out.println("You must join a game first");
            return;
        }
        System.out.println("Enter move (e.g., e2-e4): ");
        String moveStr = scanner.nextLine().trim();
        String[] positions = moveStr.split("-");
        if (positions.length != 2) {
            System.out.println("Invalid move format");
            return;
        }
        try {
            ChessPosition start = new ChessPosition(Integer.parseInt(positions[0].substring(1)), positions[0].charAt(0) - 'a' + 1);
            ChessPosition end = new ChessPosition(Integer.parseInt(positions[1].substring(1)), positions[1].charAt(0) - 'a' + 1);
            ChessMove move = new ChessMove(start, end, null);
            System.out.println("Move sent: " + moveStr);
        } catch (Exception e) {
            System.out.println("Invalid move: " + e.getMessage());
        }
    }

    private void handleResign() {
        if (currentGameId == null) {
            System.out.println("You must join a game first");
            return;
        }
        try {
            //wsClient.sendResign();
            System.out.println("Resigned from game");
            currentGameId = null;
            currentPlayerColor = null;
        } catch (Exception e) {
            System.out.println("Resign failed: " + e.getMessage());
        }
    }

    private void handleLeave() {
        if (currentGameId == null) {
            System.out.println("You must join a game first");
            return;
        }
        try {
            //wsClient.sendLeave();
            System.out.println("Left game");
            currentGameId = null;
            currentPlayerColor = null;
        } catch (Exception e) {
            System.out.println("Leave failed: " + e.getMessage());
        }
    }

    private void displayHelp() {
        System.out.println("Available commands: ");
        System.out.println("Help - Displays this help message");
        if (isLoggedIn){
            System.out.println("Logout - Logs out of the current account");
            System.out.println("Create Game - Creates a new chess game");
            System.out.println("List Games - Lists existing chess games");
            System.out.println("Join Game - Join an available chess game");
            System.out.println("Observe Game - Observe an ongoing chess game");
        } else {
            System.out.println("Login - Logs in to an existing account");
            System.out.println("Register - Registers a new user");
            System.out.println("Quit - Exit the client");
        }
    }

    private void handleLogin(Scanner scanner) {
        System.out.println("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.println("Enter password: ");
        String password = scanner.nextLine().trim();
        try {
            serverFacade.login(username, password);
            System.out.println("Login successful!");
            isLoggedIn = true;
            authToken = serverFacade.getAuthToken();
            System.out.println("You are now logged in");
            System.out.println("Type Help for a list of commands");
        } catch (IOException e){
            if (e.getMessage().contains("401")) {
                System.out.println("Login failed: Invalid username or password");
            }
            else {
                System.out.println("Login failed: " + e.getMessage());
            }
        }
    }

    private void handleRegister(Scanner scanner) {
        System.out.println("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.println("Enter password: ");
        String password = scanner.nextLine().trim();
        System.out.println("Enter email: ");
        String email = scanner.nextLine().trim();
        try {
            serverFacade.register(username, password, email);
            System.out.println("Registration successful!");
            System.out.println("Type Help for a list of commands");
            serverFacade.login(username, password);
            isLoggedIn = true;
            authToken = serverFacade.getAuthToken();
        } catch (IOException e){
            if (e.getMessage().contains("403")) {
                System.out.println("Registration failed: Username already exists");
            }
            else {
                System.out.println("Registration failed: " + e.getMessage());
            }
        }
    }

    private void handleLogout() {
        try {
            serverFacade.logout();
        } catch (Exception e){
            System.out.println("Logout failed: " + e.getMessage());
        }
        authToken = null;
        isLoggedIn = false;
        System.out.println("Logout successful!");
    }

    private void handleCreateGame(Scanner scanner) {
        if (!loginCheck()){
            return;
        }
        System.out.println("Enter game name: ");
        String gameName = scanner.nextLine().trim();
        try {
            serverFacade.createGame(gameName);
            System.out.println("Game created successfully!");
        } catch (Exception e){
            System.out.println("Game creation failed: " + e.getMessage());
        }
    }

    private void handleListGames() {
        if(!loginCheck()) {
            return;
        }
        try {
            String response = serverFacade.listGames();
            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);
            JsonArray gamesArray = jsonResponse.getAsJsonArray("games");

            if (gamesArray == null || gamesArray.size() == 0) {
                System.out.println("No games available at the moment.");
                return;
            }

            System.out.println("Available Games:");
            int index = 1;
            for (JsonElement gameElement : gamesArray) {
                JsonObject game = gameElement.getAsJsonObject();
                String gameName = game.get("gameName").getAsString();

                String whitePlayer = game.has("whiteUsername") && !game.get("whiteUsername").isJsonNull()
                        ? game.get("whiteUsername").getAsString() : "None";

                String blackPlayer = game.has("blackUsername") && !game.get("blackUsername").isJsonNull()
                        ? game.get("blackUsername").getAsString() : "None";

                System.out.printf("%d. %s - White: %s, Black: %s%n",
                        index, gameName, whitePlayer, blackPlayer);
                index++;
            }

        } catch (IOException e) {
            System.out.println("Failed to list games: Unable to connect to the server. Please try again later.");
        } catch (Exception e) {
            System.out.println("Failed to list games: An unexpected error occurred. Please try again.");
        }
    }

    private void handleJoinGame(Scanner scanner) {
        if (!loginCheck()) {
            return;
        }
        System.out.println("Enter game id: ");
        String gameId = scanner.nextLine().trim();
        System.out.println("Enter player color (white/black): ");
        String playerColor = scanner.nextLine().trim().toLowerCase();
        if (!playerColor.equals("white") && !playerColor.equals("black")) {
            System.out.println("Invalid player color, must be white or black");
            return;

        }
        try {
            serverFacade.joinGame(gameId, playerColor);
            currentPlayerColor = playerColor;
            currentGameId = gameId;
            //wsClient.connect("ws://localhost:8081", authToken, gameId);
            System.out.println("Game joined");
            displayBoard();
        } catch (IOException e){
            if (e.getMessage().contains("403")) {
                System.out.println("Unable to join: Color taken");
            } else if (e.getMessage().contains("500")) {
                System.out.println("Unable to join: ID not found");
            }
            else {
                System.out.println("Unable to join: " + e.getMessage());
            }
        }

    }
    private void handleObserveGame(Scanner scanner) {
        if (!loginCheck()) {
            return;
        }
        System.out.println("Enter game id: ");
        String gameId = scanner.nextLine().trim();
        try {
            currentGameId = gameId;
            //wsClient.connect("ws://localhost:8081", authToken, gameId);
            System.out.println("Observing game " + gameId);
            displayBoard();
        } catch (Exception e) {
            System.out.println("Observing failed: " + e.getMessage());
        }
    }


    private boolean loginCheck() {
        if (!isLoggedIn) {
            System.out.println("You must be logged in to perform this action");
            return false;
        }
        return true;
    }

    private void displayBoard(ChessBoard board, ChessGame.TeamColor playerPerspective) {
        System.out.print(EscapeSequences.ERASE_SCREEN);
        boolean isWhitePerspective = (playerPerspective == ChessGame.TeamColor.WHITE);
        int startRow = isWhitePerspective ? 8 : 1;
        int endRow = isWhitePerspective ? 1 : 8;
        int rowIncrement = isWhitePerspective ? -1 : 1;
        int startCol = isWhitePerspective ? 1 : 8;
        int endCol = isWhitePerspective ? 8 : 1;
        int colIncrement = isWhitePerspective ? 1 : -1;

        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.SET_TEXT_COLOR_WHITE + "   ");
        for (int col = startCol; isWhitePerspective ? col <= endCol : col >= endCol; col += colIncrement) {
            System.out.print(" " + (char) ('a' + col - 1) + " ");
        }
        for (int r = startRow; isWhitePerspective ? r >= endRow : r <= endRow; r += rowIncrement) {
            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.SET_TEXT_COLOR_WHITE + " " + r + " ");
            for (int c = startCol; isWhitePerspective ? c <= endCol : c >= endCol; c += colIncrement) {
                ChessPosition position = new ChessPosition(r, c);
                ChessPiece piece = board.getPiece(position);

                boolean isLightSquare = (r + c) % 2 != 0;
                String bgColor = isLightSquare ? EscapeSequences.SET_BG_COLOR_LIGHT_GREY : EscapeSequences.SET_BG_COLOR_YELLOW;

                System.out.print(bgColor);
                String pieceSymbol = getPieceSymbol(piece);
                System.out.print(pieceSymbol);
            }
        }
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.SET_TEXT_COLOR_WHITE + "   ");
        for (int col = startCol; isWhitePerspective ? col <= endCol : col >= endCol; col += colIncrement) {
            System.out.print(" " + (char) ('a' + col - 1) + " ");
        }
    }


    private String getPieceSymbol(ChessPiece piece) {
        ChessGame.TeamColor color = piece.getTeamColor();
        ChessPiece.PieceType type = piece.getPieceType();
        switch (type) {
            case PAWN:
                return color == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_PAWN : EscapeSequences.BLACK_PAWN;
            case KNIGHT:
                return color == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_KNIGHT : EscapeSequences.BLACK_KNIGHT;
            case BISHOP:
                return color == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_BISHOP : EscapeSequences.BLACK_BISHOP;
            case ROOK:
                return color == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_ROOK : EscapeSequences.BLACK_ROOK;
            case QUEEN:
                return color == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_QUEEN : EscapeSequences.BLACK_QUEEN;
            case KING:
                return color == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_KING : EscapeSequences.BLACK_KING;
            default:
                return EscapeSequences.EMPTY;
        }
    }

    public static void main(String[] args) {
        String serverURL = "http://localhost:8080";
        Client client = new Client(serverURL);
        client.run();
    }



}
