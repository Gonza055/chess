package model;

import java.util.Objects;

public final class LoginRequest {
  private final String username;
  private final String password;

  public LoginRequest(String username, String password) {
    if (username == null || username.isEmpty()) {
      throw new IllegalArgumentException("Username cannot be null or empty.");
    }
    if (password == null || password.isEmpty()) {
      throw new IllegalArgumentException("Password cannot be null or empty.");
    }
    this.username = username;
    this.password = password;
  }

  public String username() {
    return username;
  }

  public String password() {
    return password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof LoginRequest)) return false;
    LoginRequest that = (LoginRequest) o;
    return Objects.equals(username, that.username) &&
            Objects.equals(password, that.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, password);
  }

  @Override
  public String toString() {
    return "LoginRequest{" +
            "username='" + username + '\'' +
            ", password='" + password + '\'' +
            '}';
  }
}
