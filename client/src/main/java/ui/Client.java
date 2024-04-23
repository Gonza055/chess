package ui;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import chess.ChessBoard;
import java.util.Collection;
import java.util.Scanner;
import java.util.List;

import model.GameData;
import response.CreateGameResponse;
import response.JoinGameResponse;
import response.ListGamesResponse;
import response.LoginResponse;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;
public class Client implements NotificationHandler {
    public static WebSocketFacade webSocketFacade;
    public static ServerFacade serverFacade;
    public static NotificationHandler notificationHandler;
    public Client(String[] args) throws Exception {
        webSocketFacade = new WebSocketFacade("http://localhost:" + args[0], this);
        serverFacade = new ServerFacade("http://localhost", Integer.parseInt(args[0]));
    }
    private final static String defaultColor = "\u001B[0m";
    private final static String blueColor = "\u001B[34m";
    private static boolean isPlayer;
    private static ChessBoard board = null;
    private static String teamColor = null;
    private static int gameID;
    private static ChessGame chessGame;
    private static int userState;
    public static void main(String[] args) throws Exception {
        Client client = new Client(args);
        System.out.println("Welcome to CS 240 Chess\nType 'help' to get started");
        userState = 0;
        Scanner uInput = new Scanner(System.in);
        String defaultColor = "\u001B[0m";
        String blueColor = "\u001B[34m";
        String teamColor = null;
        ChessBoard board = null;
        isPlayer = false;
        while (true) {
            switch (userState) {
                case 0:
                    preLogin(args, client, uInput);
                    break;
                case 1:
                    loggedIn(args, client, uInput);
                    break;
                case 2:
                    inGame(args, client, uInput);
                    break;
                case 3:
                    waiting(args, client, uInput);
                    break;
                default:
                    System.out.println("Unknown state error occurred. Exiting..");
                    return;
            }
        }
    }
    private static void waiting(String[] args, Client client, Scanner userInput) {
        while (userState == 3) {
            System.out.println("Attempting to join...");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private static void inGame(String[] args, Client client, Scanner userInput) {
        String[] midGameInput = userInput.nextLine().split("\\s+");
        if (midGameInput[0].equals("redraw")) {
            System.out.println(board.toString(teamColor));
        }
        else if (midGameInput[0].equals("leave")) {
            try {
                client.webSocketFacade.leaveGame(serverFacade.getAuthToken(), gameID);
                userState = 1;
                System.out.println("Left game. Type 'help' for a list of commands.");
            } catch (Exception e) {
                System.out.println("Failed to leave game. Please type 'help' for a list of commands.");
            }
        }
        else if (midGameInput[0].equals("move")){
            if (isPlayer) {
                String from = midGameInput[1];
                ChessPosition fromPos = new ChessPosition(from.charAt(1) - '0', from.charAt(0) - 'a' + 1);
                String to = midGameInput[2];
                ChessPosition toPos = new ChessPosition(to.charAt(1) - '0', to.charAt(0) - 'a' + 1);
                try {
                    client.webSocketFacade.makeMove(serverFacade.getAuthToken(), gameID, new ChessMove(fromPos, toPos, null));
                } catch (Exception e){System.out.println("Failed to process move move. Ensure it is a valid move.");}
            }
            else {
                System.out.println("Cannot move as an observer. Type 'help' for a list of commands");
            }
        }
        else if (midGameInput[0].equals("resign")){
            try {
                if (isPlayer) {
                    client.webSocketFacade.resign(serverFacade.getAuthToken(), gameID);
                    userState = 1;
                    System.out.println("Resigned. Type 'help' for a list of commands.");
                    isPlayer = false;
                }
                else {
                    System.out.println("Cannot resign as an observer. Type 'help' for a list of commands");
                }
            } catch (Exception e) {
                System.out.println("Failed to resign. Please type 'help' for a list of commands.");
            }
        }
        else if (midGameInput[0].equals("highlight")){
            String pieceToCheck = midGameInput[1];
            ChessPosition piecePos = new ChessPosition(pieceToCheck.charAt(1) - '0', pieceToCheck.charAt(0) - 'a' + 1);
            Collection<ChessMove> possibleMoves = chessGame.validMoves(piecePos);
            ChessBoard highlightedBoard = board;
            System.out.println(highlightedBoard.toStringHighL(teamColor, possibleMoves, piecePos));
        }
        else if (midGameInput[0].equals("help")) {
            System.out.println("redraw" + blueColor + " - Redraw the board in its current state" + defaultColor);
            System.out.println("leave" + blueColor + " - Leave the current match" + defaultColor);
            System.out.println("move <FROM_HERE> <TO_HERE> (ColumnRow, i.e. d7)" + blueColor + " - Move a piece" + defaultColor);
            System.out.println("resign" + blueColor + " - Admit defeat" + defaultColor);
            System.out.println("highlight <PIECE_HERE>" + blueColor + " - Highlight all possible moves for a given piece" + defaultColor);
            System.out.println("help" + blueColor + " - List available commands" + defaultColor);
        }
        else {
            System.out.println("ERROR: Unknown command or incorrect syntax. Please type 'help' for a list of commands.");
        }
    }
    private static void preLogin(String[] args, Client client, Scanner userInput) {
        String[] midPreInput = userInput.nextLine().split("\\s+");
        if (midPreInput[0].equals("register") && midPreInput.length == 4) {
            String username = midPreInput[1];
            String password = midPreInput[2];
            String email = midPreInput[3];
            try {
                client.serverFacade.register(username, password, email);
                System.out.println("Logged in as " + blueColor + username + defaultColor);
                System.out.println("Type 'help' for a list of available options.");
                userState = 1;
            } catch (Exception e) {
                System.out.println("Failed to register. Be sure you have a unique username.");
            }
        }
        else if (midPreInput[0].equals("login") && midPreInput.length == 3) {
            String username = midPreInput[1];
            String password = midPreInput[2];

            try {
                LoginResponse response = client.serverFacade.login(username, password);
                System.out.println("Logged in as " + blueColor + username + defaultColor);
                System.out.println("Type 'help' for a list of available options.");
                userState = 1;
            } catch (Exception e) {
                System.out.println("Failed to log in. Check your credentials and try again.");
            }
        }
        else if (midPreInput[0].equals("quit")) {
            System.out.println("Exiting... Goodbye!");
            return;
        }
        else if (midPreInput[0].equals("help")) {
            System.out.println("register <USERNAME> <PASSWORD> <EMAIL>" + blueColor + " - Register a new user" + defaultColor);
            System.out.println("login <USERNAME> <PASSWORD>" + blueColor + " - Login with existing credentials" + defaultColor);
            System.out.println("quit" + blueColor + " - Exit the game" + defaultColor);
            System.out.println("help" + blueColor + " - List available commands" + defaultColor);
        }
        else {
            System.out.println("ERROR: Unknown command or incorrect syntax. Please type 'help' for a list of commands");
        }
    }
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    private static void loggedIn(String[] args, Client client, Scanner userInput) {
        String[] sPostInput = userInput.nextLine().split("\\s+");
        if (sPostInput[0].equals("create") && sPostInput.length == 2) {
            String gameName = sPostInput[1];
            try {
                CreateGameResponse response = client.serverFacade.create(gameName);
                System.out.println("Successfully created game " + blueColor + gameName + defaultColor);
            } catch (Exception e) {System.out.println("Failed to create game. Be sure you gave the game a unique name. Please type 'help' for a list of commands.");}
        }
        else if (sPostInput[0].equals("list")) {
            try {
                ListGamesResponse response = client.serverFacade.list();
                System.out.println("Available Games\n----------------");
                List<GameData> gameList = response.getGames();
                int listNum = 1;
                for (GameData game : gameList) {

                    System.out.println(listNum + ". Game ID - " + blueColor + game.gameID() + defaultColor + " | Game Name - " +
                            blueColor + game.gameName() + defaultColor + " | White Player - " + blueColor +
                            game.whiteUsername() + defaultColor + " | Black Player - " + blueColor + game.blackUsername() +
                            defaultColor + " |");
                    listNum++;
                }
            } catch (Exception e) {System.out.println("Couldn't retrieve list of games. Please type 'help' for a list of commands.");}
        }
        else if (sPostInput[0].equals("join") && sPostInput.length == 3) {
            if(isNumeric(sPostInput[1])) {
                int jGameID = Integer.parseInt(sPostInput[1]);
                teamColor = sPostInput[2];
                gameID = jGameID;
            } else {
                System.out.println("Invalid input: " + sPostInput[1]);
            }
            try {
                JoinGameResponse response = client.serverFacade.join(teamColor, gameID);
                ChessGame.TeamColor teamColorType = ChessGame.TeamColor.valueOf(teamColor.toUpperCase());
                try {
                    client.webSocketFacade.joinGameAsPlayer(serverFacade.getAuthToken(), gameID, teamColorType);
                    isPlayer = true;
                    userState = 3;
                } catch (Exception e){System.out.println("WebSocket connection failed. Please type 'help' for a list of commands.");}
            } catch (Exception e){System.out.println("HTTP request failed. Please type 'help' for a list of commands.");}
        }
        else if ((sPostInput[0].equals("observe") && sPostInput.length == 2) || (sPostInput[0].equals("join") && sPostInput.length == 2)) {
            int jGameID = Integer.parseInt(sPostInput[1]);
            gameID = jGameID;
            try {
                JoinGameResponse response =  client.serverFacade.join(null, gameID);
                try {
                    client.webSocketFacade.joinGameAsObserver(serverFacade.getAuthToken(), gameID);
                    isPlayer = false;
                    userState = 3;
                } catch (Exception e){System.out.println("WebSocket connection failed. Please type 'help' for a list of commands.");}
            } catch (Exception e){System.out.println("Failed to observe. Please type 'help' for a list of commands.");}
        }
        else if (sPostInput[0].equals("logout")) {
            try {
                client.serverFacade.logout();
                System.out.println("Successfully logged out.");
                System.out.println("Type 'help' to get started.");
                userState = 0;
            } catch (Exception e){System.out.println("Failed to log out. Please type 'help' for a list of commands.");}
        }
        else if (sPostInput[0].equals("quit")) {
            System.out.println("Exiting... Goodbye!");
            return;
        }
        else if (sPostInput[0].equals("help")) {
            System.out.println("create <GAME_NAME>" + blueColor + " - Create a game" + defaultColor);
            System.out.println("list" + blueColor + " - Show available games" + defaultColor);
            System.out.println("join <ID> [WHITE|BLACK|<empty>]" + blueColor + " - Join game by ID" + defaultColor);
            System.out.println("observe <ID>" + blueColor + " - Watch a game" + defaultColor);
            System.out.println("logout" + blueColor + " - Logout" + defaultColor);
            System.out.println("quit" + blueColor + " - Exit the game" + defaultColor);
            System.out.println("help" + blueColor + " - List available commands" + defaultColor);
        }
        else {System.out.println("ERROR: Unknown command or incorrect syntax. Please type 'help' for a list of commands.");}
    }
    @Override
    public void notify(String game) {
        ServerMessage servMessage = new Gson().fromJson(game, ServerMessage.class);
        switch (servMessage.getServerMessageType()) {
            case LOAD_GAME -> {
                LoadGame lGameMes = new Gson().fromJson(game, LoadGame.class);
                board = lGameMes.getGameBoard();
                chessGame = lGameMes.getChessGame();
                if (userState != 2) {
                    if (isPlayer) {
                        System.out.println("Joined successfully. Type 'help' for a list of commands.");
                    }
                    else {
                        System.out.println("Observing successfully. Type 'help' for a list of commands.");
                    }
                }
                userState = 2;
                System.out.println(board.toString(teamColor));
            }
            case NOTIFICATION -> {
                Notification notif = new Gson().fromJson(game, Notification.class);
                System.out.println(notif.getMessage());
            }
            case ERROR -> {
                Error errorMes = new Gson().fromJson(game, Error.class);
                System.out.println(errorMes.getErrorMessage());
                if (userState == 3) {
                    userState = 1;
                }
            }
        }
    }
}