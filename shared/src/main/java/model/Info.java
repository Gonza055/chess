package model;

public class Info {
  private final String username;
  private final String password;

  public Info(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String username() {
    return username;
  }

  public String password() {
    return password;
  }
}
