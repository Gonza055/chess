package model;

import java.util.Objects;

public final class UserRecord {
  private final String username;
  private final String password;
  private final String email;

  public UserRecord(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }

  public String username() {
    return username;
  }

  public String password() {
    return password;
  }

  public String email() {
    return email;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof UserRecord)) return false;
    UserRecord that = (UserRecord) o;
    return Objects.equals(username, that.username) &&
            Objects.equals(password, that.password) &&
            Objects.equals(email, that.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, password, email);
  }

  @Override
  public String toString() {
    return "UserRecord{" +
            "username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", email='" + email + '\'' +
            '}';
  }
}
