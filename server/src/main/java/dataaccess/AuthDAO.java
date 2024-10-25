package dataaccess;

import model.auth.AuthRecord;
import java.util.ArrayList;
import java.util.UUID;

public class AuthDAO {

  private ArrayList<AuthRecord> recordsContainer;

  public AuthDAO() {
    initRecords();
  }

  private void initRecords() {
    if (recordsContainer == null) {
      recordsContainer = new ArrayList<>();
    }
  }

  public AuthRecord newAuth(String username) {
    AuthRecord auth = null;
    if (username != null && !username.isEmpty()) {
      String token = UUID.randomUUID().toString();
      auth = new AuthRecord(token, username);

      boolean isAdded = addAuthRec(auth);
      if (!isAdded) {
        throw new RuntimeException("Failed to add authentication record.");
      }
    }
    return auth;
  }

  private boolean addAuthRec(AuthRecord record) {
    return recordsContainer.add(record);
  }

  public AuthRecord addAuth(String authToken) {
    if (authToken == null || authToken.isEmpty()) {
      return null;
    }

    for (int i = 0; i < recordsContainer.size(); i++) {
      AuthRecord currentRecord = recordsContainer.get(i);
      if (currentRecord != null && authToken.equals(currentRecord.authToken())) {
        return currentRecord;
      }
    }
    return null;
  }

  public void deleteSingleAuth(String authToken) {
    if (authToken == null || authToken.isEmpty()) {
      return;
    }

    for (int i = 0; i < recordsContainer.size(); i++) {
      AuthRecord currentRecord = recordsContainer.get(i);
      if (currentRecord != null && authToken.equals(currentRecord.authToken())) {
        recordsContainer.remove(i);
        break;
      }
    }
  }

  public void deleteAllAuths() {
    if (recordsContainer != null && !recordsContainer.isEmpty()) {
      while (!recordsContainer.isEmpty()) {
        recordsContainer.remove(0);
      }
    }

    initRecords();
  }
}