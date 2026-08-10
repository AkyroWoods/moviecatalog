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

    public void insertMovie(Movie movie) {
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
                    movie.setMovieId(keyValue.getInt(1));
                }
            }
            insertGenres(movie);
        } catch (Exception e) {
            System.out.println("Error occurred:" + e.getMessage());
        } finally {
            disconnect();
        }
    }

    private void insertGenres(Movie movie) {
        String insertGenre = "INSERT INTO genres (genre) VALUES (?) ON CONFLICT DO NOTHING";
        String selectGenre = "SELECT genre_id FROM genres where genre=?";
        String insertIntoMovie_GenresTable = "INSERT INTO movie_genres (movie_id, genre_id) VALUES (?,?)";
        connect();

        int genreValue = 0;
        try {
            PreparedStatement statement = jdbcConnection.prepareStatement(insertGenre, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement select = jdbcConnection.prepareStatement(selectGenre);
            PreparedStatement join = jdbcConnection.prepareStatement(insertIntoMovie_GenresTable);

            for (String genres : movie.getGenreList()) {
                select.setString(1, genres);
                ResultSet genreId = select.executeQuery();
                if (genreId.next()) {
                    genreValue = genreId.getInt(1);

                } else {
                    statement.setString(1, genres);
                    statement.executeUpdate();
                    genreId = statement.getGeneratedKeys();
                    if (genreId.next()) {
                        genreValue = genreId.getInt(1);
                    } else {
                        select.setString(1, genres);
                        genreId = select.executeQuery();
                        genreId.next();
                        genreValue = genreId.getInt(1);
                    }
                }
                join.setInt(1, movie.getMovieId());
                join.setInt(2, genreValue);
                join.executeUpdate();
                genreId.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            disconnect();
        }
    }

    public int deleteMovie(Movie movie) {
        String sql = "DELETE FROM movies WHERE movie_id=?";

        try {
            connect();
            PreparedStatement statement = jdbcConnection.prepareStatement(sql);
            statement.setInt(1, movie.getMovieId());
            return statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        } finally {
            disconnect();
        }
    }

    public Movie getMovieByTitle(String title) {
        Movie movie = new Movie();
        String sql = "SELECT * FROM movies where title=?";

        try {
            connect();
            PreparedStatement statement = jdbcConnection.prepareStatement(sql);
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                movie.setMovieId(resultSet.getInt(1));
                movie.setTitle(resultSet.getString(2));
                movie.setReleaseYear(resultSet.getInt(3));
                movie.setRuntime(resultSet.getInt(5));
                movie.setDirector(resultSet.getString(6));
                movie.setRating(resultSet.getString(7));
                movie.setDescription(resultSet.getString(8));
                movie.setFormat(Formats.valueOf(resultSet.getString(9)));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            disconnect();
        }
        return movie;
    }

}
