package dataAccess;

import model.GameData;

import java.util.List;

public interface GameDAO {
    void updIndex();
    void crtGame(GameData game);
    int getCurrID();
    GameData gameGet(int index);
    void gameSet(int index, GameData game);
    int sizeGet();
    void clearGList();
    List<GameData> returnGameList();
}
