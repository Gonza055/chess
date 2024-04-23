package response;
import java.util.List;
import model.GameData;
public class ListGamesResponse {
    public List<GameData> games;
    public String message;
    public int status;
    public ListGamesResponse(List<GameData> games, String message, int status) {
        this.games = games;
        this.message = message;
        this.status = status;
    }
    public List<GameData> getGames() {
        return games;
    }
}
