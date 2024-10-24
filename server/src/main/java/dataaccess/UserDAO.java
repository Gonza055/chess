package dataaccess;

import model.UserRecord;

import java.util.HashMap;
import java.util.Map;

public class UserDAO {

  private final Map<String, UserRecord> userRecords;

  public UserDAO() {
    userRecords = new HashMap<>();
  }

  public UserRecord findUser(String username) {
    return userRecords.get(username);
  }

  public void createUser(UserRecord user) {
    if (!userRecords.containsKey(user.info().username())) {
      userRecords.put(user.info().username(), user);
    }
  }

  public void deleteUsers() {
    userRecords.clear();
  }
}
