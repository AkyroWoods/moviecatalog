package akyrowoods.moviecatalog;

import java.sql.*;


public class MovieDao {
    private final String jdbcUrl;
    private final String username;
    private final String password;
    private Connection jdbcConnection;

    public MovieDao(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public void connect() {
        try {
            this.jdbcConnection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Connection Successful");
        } catch (Exception e) {
            System.out.println("Database access error or url is null " + e.getMessage());
        }
    }

    public void disconnect() {
        try {
            if (jdbcConnection != null && !jdbcConnection.isClosed()) {
                jdbcConnection.close();
                System.out.println("Connection Disconnected");
            }
        } catch (Exception e) {
            System.out.println("Database access error: " + e.getMessage());
        }
    }

    public int insertMovie(Movie movie) {
        String sql = "INSERT INTO movies (title, release_year, runtime, director, rating, description, format) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            connect();
            PreparedStatement statement = jdbcConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, movie.getTitle());
            statement.setInt(2, movie.getReleaseYear());
            statement.setInt(3, movie.getRuntime());
            statement.setString(4, movie.getDirector());
            statement.setString(5, movie.getRating());
            statement.setString(6, movie.getDescription());
            statement.setString(7, movie.getFormat());

            int insertedRow = statement.executeUpdate();

            if (insertedRow > 0) {
                ResultSet keyValue = statement.getGeneratedKeys();
                if (keyValue.next()) {
                    return keyValue.getInt(1);
                }

            }


        } catch (Exception e) {
            System.out.println("Error occurred:" + e.getMessage());
        }
        return -1;
    }


}
