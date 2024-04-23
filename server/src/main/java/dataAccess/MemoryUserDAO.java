package dataAccess;
import model.UserData;
import java.util.ArrayList;
import java.util.List;
public class MemoryUserDAO implements UserDAO{
    public List<UserData> userList = new ArrayList<>();
    @Override
    public void updIndex(){}
    @Override
    public void crtUser(UserData newUser) {
        userList.add(newUser);
    }
    @Override
    public UserData userGet(int index) {
        return userList.get(index);
    }
    @Override
    public int sizeGet() {
        return userList.size();
    }
    @Override
    public void clrUList() {
        userList.clear();
    }
}
