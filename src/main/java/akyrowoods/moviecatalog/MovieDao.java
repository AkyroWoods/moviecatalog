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
            Class.forName("org.postgresql.Driver");
            this.jdbcConnection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    public void disconnect() {
        try {
            if (jdbcConnection != null && !jdbcConnection.isClosed()) {
                jdbcConnection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not close database connection", e);
        }
    }

    public boolean insertMovie(Movie movie) {
        String sql = "INSERT INTO movies (title, release_year, runtime, director, rating, description, format, poster_url) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        connect();
        try (PreparedStatement statement = jdbcConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, movie.getTitle());
            statement.setInt(2, movie.getReleaseYear());
            statement.setInt(3, movie.getRuntime());
            statement.setString(4, movie.getDirector());
            statement.setString(5, movie.getRating());
            statement.setString(6, movie.getDescription());
            statement.setString(7, movie.getFormat().toString());
            statement.setString(8, movie.getPosterUrl());

            int insertedRow = statement.executeUpdate();
            if (insertedRow > 0) {
                ResultSet keyValue = statement.getGeneratedKeys();
                if (keyValue.next())
                    movie.setMovieId(keyValue.getInt(1));

                insertGenres(movie);
            }
            return insertedRow > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Could not insert " + movie.getTitle()+  "into the database", e);
        } finally {
            disconnect();
        }
    }

    public List<Movie> listAllMovies() {
        List<Movie> collection = new ArrayList<>();
        String sql = "Select * from movies";

        connect();
        try (Statement statement = jdbcConnection.createStatement();
              ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                int movieId = rs.getInt("movie_id");
                String title = rs.getString("title");
                int releaseYear = rs.getInt("release_year");
                int runTime = rs.getInt("runtime");
                String director = rs.getString("director");
                String rating = rs.getString("rating");
                String description = rs.getString("description");
                Formats format = Formats.valueOf(rs.getString("format"));
                String posterUrl = rs.getString("poster_url");

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
                movie.setPosterUrl(posterUrl);
                collection.add(movie);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to list all movies", e);
        } finally {
            disconnect();
        }
        return collection;
    }

    public List<String> listAllGenres() throws SQLException {
        List<String> genres = new ArrayList<>();
        String sql = "Select genre from genres";

        connect();
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            genres.add(rs.getString("genre"));
        }
        disconnect();
        return genres;
    }

    public List<String> gatherGenres(Movie movie) {
        List<String> genres = new ArrayList<>();
        String sql = "select m.movie_id, g.genre from movie_genres mg" +
                " inner join movies m  on m.movie_id = mg.movie_id " +
                "inner join genres g on g.genre_id = mg.genre_id " +
                "where m.movie_id = ?";
        connect();

        try (PreparedStatement statement = jdbcConnection.prepareStatement(sql)
        ) {
            statement.setInt(1, movie.getMovieId());
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                genres.add(rs.getString("genre"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not gather genres for: " +
                    movie.getTitle() + " " + e.getMessage());
        } finally {
            disconnect();
        }
        return genres;
    }

    private void insertGenres(Movie movie) throws SQLException {
        String insertGenre = "INSERT INTO genres (genre) VALUES (?) ON CONFLICT (genre) DO UPDATE SET genre=EXCLUDED.genre RETURNING genre_id";
        String insertIntoMovie_GenresTable = "INSERT INTO movie_genres (movie_id, genre_id) VALUES (?,?)";

        PreparedStatement statement = jdbcConnection.prepareStatement(insertGenre);
        PreparedStatement join = jdbcConnection.prepareStatement(insertIntoMovie_GenresTable);

        for (String genres : movie.getGenreList()) {
            statement.setString(1, genres);
            ResultSet genreId = statement.executeQuery();

            int genreValue = 0;
            if (genreId.next()) {
                genreValue = genreId.getInt(1);
            }

            join.setInt(1, movie.getMovieId());
            join.setInt(2, genreValue);
            join.executeUpdate();
            genreId.close();
        }
    }

    public boolean updateMovie(Movie movie) {
        String sql =
                "UPDATE movies " +
                        "SET title=?, release_year=?, format=?, description=? " +
                        "WHERE movie_id=?";

        String deleteGenres = "DELETE FROM movie_genres where movie_id=?";
        connect();

        try (
                PreparedStatement statement = jdbcConnection.prepareStatement(sql);
                PreparedStatement genres = jdbcConnection.prepareStatement(deleteGenres);
        ) {

            genres.setInt(1, movie.getMovieId());
            genres.executeUpdate();

            statement.setString(1, movie.getTitle());
            statement.setInt(2, movie.getReleaseYear());
            statement.setString(3, movie.getFormat().toString());
            statement.setString(4, movie.getDescription());
            statement.setInt(5, movie.getMovieId());
            boolean update = statement.executeUpdate() > 0;

            if (update) {
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
        String movieStmnt = "DELETE FROM movies WHERE movie_id=?";
        String joinStmnt = "DELETE FROM movie_genres WHERE movie_id=?";
        connect();

        try (
                PreparedStatement movieTable = jdbcConnection.prepareStatement(movieStmnt);
                PreparedStatement joinTable = jdbcConnection.prepareStatement(joinStmnt);
        ) {
            movieTable.setInt(1, movie.getMovieId());
            joinTable.setInt(1, movie.getMovieId());
            return joinTable.executeUpdate() + movieTable.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Failed to update movie: " + e.getMessage());
            return false;
        } finally {
            disconnect();
        }
    }

    public Movie getMovieByTitle(String title) throws SQLException {
        Movie movie = new Movie();
        String sql = "SELECT * FROM movies where title=?";

        connect();
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setString(1, title);
        ResultSet rs = statement.executeQuery();

        return getMovie(movie, rs);
    }

    private Movie getMovie(Movie movie, ResultSet rs) throws SQLException {
        if (rs.next()) {
            movie.setMovieId(rs.getInt("movie_id"));
            movie.setTitle(rs.getString("title"));
            movie.setReleaseYear(rs.getInt("release_year"));
            movie.setRuntime(rs.getInt("runtime"));
            movie.setDirector(rs.getString("director"));
            movie.setRating(rs.getString("rating"));
            movie.setDescription(rs.getString("description"));
            movie.setFormat(Formats.valueOf(rs.getString("format")));
            movie.setPosterUrl(rs.getString("poster_url"));
        }
        disconnect();
        return movie;
    }

    public Movie getMovieById(int id) throws SQLException {
        Movie movie = new Movie();
        String sql = "SELECT * FROM movies where movie_id=?";

        connect();
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();

        return getMovie(movie, rs);
    }

}