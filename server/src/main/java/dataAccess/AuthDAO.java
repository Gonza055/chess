package dataAccess;

import model.AuthData;

public interface AuthDAO {
    void updIndex();
    void crtAuth(AuthData authData);
    void rmvAuth(AuthData authData);
    AuthData getAuthWID(int index);
    model.AuthData getAuth(String username);
    String userGet(String authToken);
    int sizeGet();
    void clrAuthList();

    void clearAuthList();
}
