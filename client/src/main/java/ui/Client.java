package ui;

import java.util.Scanner;
import chess.*;

public class Client {
    private final String serverURL;
    private final ServerFacade serverFacade;
    private boolean isRunning;
    private boolean isLoggedIn;
    private ChessBoard board;
    private String currentPlayerColor;
    private String authToken;

    public Client(String serverURL) {
        this.serverURL = serverURL;
        this.serverFacade = new ServerFacade(serverURL);
        this.isRunning = true;
        this.isLoggedIn = false;
        this.board = new ChessBoard();
        this.board.resetBoard();
        this.currentPlayerColor = null;
    }

    public void run() {
        System.out.println("Welcome to 240 Chess! Type Help to get started");
        displayBoard();
        Scanner scanner = new Scanner(System.in);
        while(isRunning) {
            String command = scanner.nextLine().trim().toLowerCase();
            handleCommand(command, scanner);
        }
        scanner.close();
    }

    private void handleCommand(String command, Scanner scanner) {
        if (isLoggedIn) {
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
            case "play game":
                handleJoinGame(scanner);
            case "observe game":
                handleObserveGame(scanner);
                break;
            default:
                System.out.println("Invalid command, type Help for a list of commands");
        }
    }

    private void displayHelp() {
        System.out.println("Available commands: ");
        System.out.println("Help - Displays this help message");
        if (isLoggedIn){
            System.out.println("Logout - Logs out of the current account");
            System.out.println("Create Game - Creates a new chess game");
            System.out.println("List Games - Lists existing chess games");
            System.out.println("Play Game - Join an available chess game");
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
            String response = serverFacade.login(username, password);
            System.out.println("Login successful " + response);
            isLoggedIn = true;
            authToken = serverFacade.getAuthToken();
            System.out.println("You are now logged in");
            System.out.println("Type Help for a list of commands");
        } catch (Exception e){
            System.out.println("Login failed: " + e.getMessage());
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
            String response = serverFacade.register(username, password, email);
            System.out.println("Registration successful " + response);
        } catch (Exception e){
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private void handleLogout() {
        String response = null;
        try {
            response = serverFacade.logout();
        } catch (Exception e){
            System.out.println("Logout failed: " + e.getMessage());
        }
        authToken = null;
        isLoggedIn = false;
        System.out.println("Logout successful " + response);
        displayBoard();
    }

    private void handleCreateGame(Scanner scanner) {
        if (!loginCheck()){
            return;
        }
        System.out.println("Enter game name: ");
        String gameName = scanner.nextLine().trim();
        try {
            String response = serverFacade.createGame(gameName);
            System.out.println("Game created " + response);
        } catch (Exception e){
            System.out.println("Game creation failed: " + e.getMessage());
        }
    }

    private void handleListGames(){
        if (!loginCheck()){
            return;
        }
        try {
            String response = serverFacade.listGames();
            System.out.println("Games: " + response);
        } catch (Exception e){
            System.out.println("Game listing failed: " + e.getMessage());
        }
    }

    private void handleJoinGame(Scanner scanner) {
        if (!loginCheck()){
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
            String response = serverFacade.joinGame(gameId, playerColor);
            currentPlayerColor = playerColor;
            System.out.println("Game joined " + response);
            displayBoard();
        } catch (Exception e){
            System.out.println("Game joining failed: " + e.getMessage());
        }

    }
    private void handleObserveGame(Scanner scanner) {
        return;
    }


    private boolean loginCheck() {
        if (!isLoggedIn) {
            System.out.println("You must be logged in to perform this action");
            return false;
        }
        return true;
    }

    private void displayBoard() {
        System.out.println("Board");
    }

    public static void main(String[] args) {
        String serverURL = "http://localhost:8080";
        Client client = new Client(serverURL);
        client.run();
    }



}
