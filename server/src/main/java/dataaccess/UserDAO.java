package dataaccess;

import model.user.UserRecord;
import java.util.ArrayList;
import java.util.Iterator;

public class UserDAO {

  private ArrayList<UserRecord> userStorage;

  public UserDAO() {
    initStorage();
  }

  private void initStorage() {
    if (userStorage == null) {
      userStorage = new ArrayList<>();
    }
  }

  public UserRecord findUser(String username) {
    if (username == null || username.isEmpty()) {
      return null;
    }

    UserRecord foundUser = null;

    Iterator<UserRecord> iterator = userStorage.iterator();
    while (iterator.hasNext()) {
      UserRecord currentUser = iterator.next();
      if (currentUser != null && currentUser.username().equals(username)) {
        foundUser = currentUser;
        break;
      }
    }

    return foundUser;
  }

  public void newUser(UserRecord newUser) {
    if (newUser != null) {
      boolean added = addUser(newUser);
      if (!added) {
        throw new RuntimeException("Failed to create user.");
      }
    } else {
      throw new IllegalArgumentException("User cannot be null.");
    }
  }

  private boolean addUser(UserRecord user) {
    return userStorage.add(user);
  }

  public void deleteAllUsers() {
    if (userStorage != null && !userStorage.isEmpty()) {
      clearUsers();
    } else {
      reinitStorage();
    }
  }

  private void clearUsers() {
    while (!userStorage.isEmpty()) {
      userStorage.remove(0);
    }
  }

  private void reinitStorage() {
    userStorage = new ArrayList<>();
  }
}