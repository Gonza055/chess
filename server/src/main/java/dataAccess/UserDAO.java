package dataAccess;
import model.UserData;
public interface UserDAO {
    void updIndex();
    void crtUser(UserData newUser);
    model.UserData userGet(int index);
    int sizeGet();
    void clrUList();
}