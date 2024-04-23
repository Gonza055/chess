package dataAccess;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import model.UserData;
public class SQLUserDAO implements UserDAO {
    private int userIndex = 0;
    public void updIndex() {
        String squeuel= "SELECT MAX(ID) AS max_id FROM users";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement stmt = con.prepareStatement(squeuel);
             ResultSet res = stmt.executeQuery()) {
            if (res.next()) {
                this.userIndex = res.getInt("max_id") + 1;
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void crtUser(UserData newUser) {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try (PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO users (ID, username, password, email) VALUES(?, ?, ?, ?)")) {
            preparedStatement.setInt(1, this.userIndex);
            preparedStatement.setString(2, newUser.username());
            preparedStatement.setString(3, newUser.password());
            preparedStatement.setString(4, newUser.email());
            preparedStatement.executeUpdate();
            this.userIndex = this.userIndex + 1;
        } catch (SQLException e) {
            try {
                throw new DataAccessException(e.getMessage());
            } catch (DataAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void clrUList() {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement preparedStatement = conn.prepareStatement("TRUNCATE TABLE users")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            try {
                throw new DataAccessException(e.getMessage());
            } catch (DataAccessException ex) {
                throw new RuntimeException(ex);
            }
        }

        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        userIndex = 0;
    }
    @Override
    public UserData userGet(int index) {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM users WHERE ID = (?)")) {
            preparedStatement.setInt(1, index);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String email = resultSet.getString("email");

                    try {
                        conn.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    return new UserData(username, password, email);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        tryClosing(conn);

        return null;
    }

    private void tryClosing(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int sizeGet() {
        return this.userIndex + 1;
    }
}
