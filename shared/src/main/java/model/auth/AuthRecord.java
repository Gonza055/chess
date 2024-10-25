package model;

import java.util.Objects;

public final class AuthRecord {
  private final String authToken;
  private final String username;

  public AuthRecord(String authToken, String username) {
    if (authToken == null || authToken.isEmpty()) {
      throw new IllegalArgumentException("Auth token cannot be null or empty.");
    }
    if (username == null || username.isEmpty()) {
      throw new IllegalArgumentException("Username cannot be null or empty.");
    }
    this.authToken = authToken;
    this.username = username;
  }

  public String authToken() {
    return authToken;
  }

  public String username() {
    return username;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof AuthRecord)) return false;
    AuthRecord that = (AuthRecord) o;
    return Objects.equals(authToken, that.authToken) &&
            Objects.equals(username, that.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authToken, username);
  }

  @Override
  public String toString() {
    return "AuthRecord{" +
            "authToken='" + authToken + '\'' +
            ", username='" + username + '\'' +
            '}';
  }
}
