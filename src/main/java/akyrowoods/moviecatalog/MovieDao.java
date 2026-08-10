package akyrowoods.moviecatalog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public boolean insertMovie(Movie movie) {
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
            return insertedRow > 0;
        } catch (Exception e) {
            System.out.println("Error occurred:" + e.getMessage());
            return false;
        } finally {
            disconnect();
        }
    }

    public List<Movie> listAllMovies() {
        List<Movie> collection = new ArrayList<>();
        String sql = "Select * from movies";
        try {
            connect();
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                int movieId = rs.getInt("movie_id");
                String title = rs.getString("title");
                int releaseYear = rs.getInt("release_year");
                int runTime = rs.getInt("runtime");
                String director = rs.getString("director");
                String rating =  rs.getString("rating");
                String description = rs.getString("description");
                Formats format = Formats.valueOf(rs.getString("format"));

                Movie movie = new Movie();
                movie.setMovieId(movieId);
                movie.setTitle(title);
                movie.setReleaseYear(releaseYear);
                movie.setRuntime(runTime);
                movie.setDirector(director);
                movie.setRating(rating);
                movie.setDescription(description);
                movie.setFormat(format);
                movie.setGenreList(gatherGenres(movie));
                collection.add(movie);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            disconnect();
        }
        return collection;
    }

    private List<String> gatherGenres(Movie movie) {
        List<String> genres = new ArrayList<>();
        String sql =  "select m.movie_id, g.genre from movie_genres mg" +
                " inner join movies m  on m.movie_id = mg.movie_id " +
                "inner join genres g on g.genre_id = mg.genre_id " +
                "where m.movie_id = ?";

        try {
            connect();
            PreparedStatement statement = jdbcConnection.prepareStatement(sql);
            statement.setInt(1, movie.getMovieId());
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
            genres.add(rs.getString("genre"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return genres;
    }

    private void insertGenres(Movie movie) {
        String insertGenre = "INSERT INTO genres (genre) VALUES (?) ON CONFLICT (genre) DO UPDATE SET genre=EXCLUDED.genre RETURNING genre_id";
        String insertIntoMovie_GenresTable = "INSERT INTO movie_genres (movie_id, genre_id) VALUES (?,?)";

        try {
            for (String genres : movie.getGenreList()) {
                PreparedStatement statement = jdbcConnection.prepareStatement(insertGenre);
                statement.setString(1, genres);
                ResultSet genreId = statement.executeQuery();

                int genreValue = 0;
                if (genreId.next()) {
                    genreValue = genreId.getInt(1);
                }

                PreparedStatement join = jdbcConnection.prepareStatement(insertIntoMovie_GenresTable);
                join.setInt(1, movie.getMovieId());
                join.setInt(2, genreValue);
                join.executeUpdate();
                genreId.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean updateMovie(Movie movie) {
        String sql = "UPDATE movies SET title =?, release_year=?, runtime=?, director=?, rating=?, description=?, format=? " + "WHERE movie_id=?";
        String deleteGenres = "DELETE FROM movie_genres where movie_id=?";

        try {
            connect();
            PreparedStatement statement = jdbcConnection.prepareStatement(sql);
            PreparedStatement genres = jdbcConnection.prepareStatement(deleteGenres);

            genres.setInt(1, movie.getMovieId());
            genres.executeUpdate();

            statement.setString(1, movie.getTitle());
            statement.setInt(2, movie.getReleaseYear());
            statement.setInt(3, movie.getRuntime());
            statement.setString(4, movie.getDirector());
            statement.setString(5, movie.getRating());
            statement.setString(6, movie.getDescription());
            statement.setString(7, movie.getFormat());
            statement.setInt(8, movie.getMovieId());


            boolean update =  statement.executeUpdate() > 0;

            if(update) {
                insertGenres(movie); //update genres
            }

            return update;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            disconnect();
        }
    }

    public boolean deleteMovie(Movie movie) {
        String sql = "DELETE FROM movies WHERE movie_id=?";

        try {
            connect();
            PreparedStatement statement = jdbcConnection.prepareStatement(sql);
            statement.setInt(1, movie.getMovieId());
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
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
