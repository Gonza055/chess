package dataAccess;
import model.AuthData;
import java.util.ArrayList;
import java.util.List;
public abstract class MemoryAuthDAO implements AuthDAO {
    public List<AuthData> authList = new ArrayList<>();
    @Override
    public void updIndex(){}
    @Override
    public void crtAuth(AuthData authData){
        authList.add(authData);
    }
    @Override
    public void rmvAuth(AuthData authData){
        authList.remove(authData);
    }
    public AuthData getAuthWID(int index){
        return authList.get(index);
    }
    @Override
    public String userGet(String authToken) {
        for (int i = 0; i < authList.size(); i = i + 1) {
            if (authList.get(i).username().equals(authToken)){
                return authList.get(i).username();
            }
            else{
                return null;
            }
        }
        return null;
    }
    @Override
    public int sizeGet() {
        return authList.size();
    }
    @Override
    public void clearAuthList() {
        authList.clear();
    }
}
