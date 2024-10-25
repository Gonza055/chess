package service;

import chess.ChessGame;
import logic.Exception;
import logic.Service;
import model.AuthRecord;
import model.LoginRequest;
import model.UserRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {

    @Test
    @DisplayName("Registers users with complete information")
    public void testRegisterUser() throws Exception {
        Service service = new Service();
        UserRecord newUser = new UserRecord("testUser", "testPass", "email@gmail");
        UserRecord newUser1 = new UserRecord("testUser1", "testPass1", "anotherEmail@gmail");

        // Registration works
        assertDoesNotThrow(() -> service.register(newUser));
        AuthRecord auth = service.register(newUser1);
        assertNotNull(auth.authToken());
    }

    @Test
    @DisplayName("Registers users with complete information")
    public void testRegisterUserNegative() throws Exception {
        Service service = new Service();
        UserRecord incompleteUser = new UserRecord("testUser", "noEmail", null);

        // Registration forces a full user
        assertThrows(Exception.class, () -> service.register(incompleteUser));
    }

    @Test
    @DisplayName("Logs in registered users")
    public void testLogin() throws Exception {
        Service service = new Service();
        UserRecord newUser = new UserRecord("testUser", "testPass", "email@gmail");
        LoginRequest loginRequest = new LoginRequest("testUser", "testPass");

        // Login Works and returns auth information
        AuthRecord auth = service.register(newUser);
        assertDoesNotThrow(() -> service.login(loginRequest));
        assertNotNull(service.login(loginRequest).authToken());

    }

    @Test
    @DisplayName("Does not allow bad logins")
    public void testLoginNegative() throws Exception {
        Service service = new Service();
        UserRecord newUser = new UserRecord("testUser", "testPass", "email@gmail");
        LoginRequest badLogin = new LoginRequest("idk", "notPassword");
        AuthRecord auth = service.register(newUser);

        // Login Does not Work if wrong user or password
        assertThrows(Exception.class, () -> service.login(badLogin));
    }

    @Test
    @DisplayName("Logs out registered users")
    public void testLogout() throws Exception {
        Service service = new Service();
        UserRecord newUser = new UserRecord("testUser", "testPass", "email@gmail");

        // Logs out a user using authToken
        AuthRecord auth = service.register(newUser);
        assertDoesNotThrow(() -> service.logout(auth.authToken()));
    }

    @Test
    @DisplayName("Doesn't let users perform auth actions after logout")
    public void testLogoutNegative() throws Exception {
        Service service = new Service();
        UserRecord newUser = new UserRecord("testUser", "testPass", "email@gmail");

        // Logs out a user using authToken
        AuthRecord auth = service.register(newUser);
        service.logout(auth.authToken());

        assertThrows(Exception.class, () -> service.verify(auth.authToken()));
    }



    @Test
    @DisplayName("Creates games")
    public void testCreateGames() throws Exception {
        Service service = new Service();
        UserRecord newUser = new UserRecord("testUser", "testPass", "email@gmail");

        AuthRecord auth = service.register(newUser);
        assertDoesNotThrow(() -> service.newGame(auth.authToken(), "GameName"));
    }

    @Test
    @DisplayName("Creates games only when logged in")
    public void testCreateGamesNegative() throws Exception {
        Service service = new Service();
        UserRecord newUser = new UserRecord("testUser", "testPass", "email@gmail");

        AuthRecord auth = service.register(newUser);
        assertDoesNotThrow(() -> service.newGame(auth.authToken(), "GameName"));

        // Cannot create a game when name in use or when not logged in
        assertThrows(Exception.class, () -> service.newGame(auth.authToken(), "GameName"));
        assertThrows(Exception.class, () -> service.newGame("notAToken", "GameName1"));
    }

    @Test
    @DisplayName("Lists Games")
    public void testListGames() throws Exception {
        Service service = new Service();
        UserRecord newUser = new UserRecord("testUser", "testPass", "email@gmail");

        AuthRecord auth = service.register(newUser);
        service.newGame(auth.authToken(), "GameName");

        // Doesn't throw error and returns items when logged in
        assertDoesNotThrow(() -> service.listGames(auth.authToken()));
        assertNotNull(service.listGames(auth.authToken()).get(0));
    }

    @Test
    @DisplayName("Lists Games only when Logged In")
    public void testListGamesNegative() throws Exception {
        Service service = new Service();
        UserRecord newUser = new UserRecord("testUser", "testPass", "email@gmail");

        AuthRecord auth = service.register(newUser);
        service.newGame(auth.authToken(), "GameName");

        // Doesn't throw error and returns items when logged in
        service.listGames(auth.authToken());

        assertThrows(Exception.class, () -> service.listGames("notAToken"));
    }

    @Test
    @DisplayName("Clears DB")
    public void testClearDB() throws Exception {
        Service service = new Service();
        UserRecord newUser = new UserRecord("testUser", "testPass", "email@gmail");

        AuthRecord auth = service.register(newUser);
        service.newGame(auth.authToken(), "GameName");
        assertDoesNotThrow(service::deleteDB);
    }

    @Test
    @DisplayName("Can't clear DB without auth")
    public void testClearDBNegative() throws Exception {
        Service service = new Service();
        UserRecord newUser = new UserRecord("testUser", "testPass", "email@gmail");

        AuthRecord auth = service.register(newUser);
        service.newGame(auth.authToken(), "GameName");

        // Can't query games once db is gone
        assertDoesNotThrow(service::deleteDB);
        assertThrows(Exception.class, () -> service.listGames(auth.authToken()));
    }
    @Test
    @DisplayName("Joins Existing Games")
    public void testJoinGames() throws Exception {
        Service service = new Service();
        UserRecord newUser = new UserRecord("testUser", "testPass", "email@gmail");

        AuthRecord auth = service.register(newUser);
        int id = service.newGame(auth.authToken(), "GameName");

        assertDoesNotThrow(() -> service.joinGame(auth.authToken(), ChessGame.TeamColor.WHITE, id));
    }

    @Test
    @DisplayName("Joins Existing Games covering edge cases")
    public void testJoinGamesNegative() throws Exception {
        Service service = new Service();
        UserRecord newUser = new UserRecord("testUser", "testPass", "email@gmail");

        AuthRecord auth = service.register(newUser);
        int id = service.newGame(auth.authToken(), "GameName");

        // Can only join once
        assertDoesNotThrow(() -> service.joinGame(auth.authToken(), ChessGame.TeamColor.WHITE, id));
        assertThrows(Exception.class, () -> service.joinGame(auth.authToken(), ChessGame.TeamColor.WHITE, id));

        // Needs auth to join games
        assertThrows(Exception.class, () -> service.joinGame("noAuth", ChessGame.TeamColor.BLACK, id));

        // Must join existing game
        assertThrows(Exception.class, () -> service.joinGame(auth.authToken(), ChessGame.TeamColor.BLACK, 75498254));
    }
}



