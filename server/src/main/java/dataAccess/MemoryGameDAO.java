package dataAccess;
import model.GameData;
import java.util.ArrayList;
import java.util.List;
public class MemoryGameDAO implements GameDAO {
    public List<GameData> gameList = new ArrayList<>();
    public int currentID = 1;
    @Override
    public void updIndex(){}
    @Override
    public void crtGame(GameData game) {
        currentID += 1;
        gameList.add(game);
    }
    @Override
    public int getCurrID() {
        return currentID;
    }
    @Override
    public GameData gameGet(int index) {
        return gameList.get(index);
    }
    @Override
    public void gameSet(int index, GameData game) {
        gameList.set(index, game);
    }
    @Override
    public int sizeGet() {
        return gameList.size();
    }
    @Override
    public void clearGList() {
        gameList.clear();
    }
    @Override
    public List<GameData> returnGameList() {
        return gameList;
    }
}