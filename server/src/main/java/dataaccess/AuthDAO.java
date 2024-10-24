package dataaccess;

import model.AuthRecord;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthDAO {

  private final Map<String, AuthRecord> authRecords;

  public AuthDAO() {
    authRecords = new HashMap<>();
  }

  public AuthRecord createAuth(String username) {
    String token = generateToken();
    AuthRecord authRecord = new AuthRecord(token, username);
    authRecords.put(token, authRecord);
    return authRecord;
  }

  public AuthRecord getAuthByToken(String authToken) {
    return authRecords.get(authToken);
  }

  public void deleteAuthByToken(String authToken) {
    authRecords.remove(authToken);
  }

  public void clearAuthRecords() {
    authRecords.clear();
  }

  private String generateToken() {
    return UUID.randomUUID().toString();
  }
}
