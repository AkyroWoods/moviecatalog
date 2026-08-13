package akyrowoods.moviecatalog;


import java.io.IOException;
import java.io.Serial;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.sun.net.httpserver.Request;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({"/new", "/insert", "/update", "/edit", "/delete", "/list", "/details", "/search"})
public class ControllerServlet extends HttpServlet{
    @Serial
    private static final long serialVersionUID = 1L;
    private MovieDao movieDao;
    private MovieApiClient api;
    private JsonToMovie movieConverter;

    public void init() {
        String jdbcUrl = getServletContext().getInitParameter("jdbcUrl");
        String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
        String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");
        movieDao = new MovieDao(jdbcUrl, jdbcUsername, jdbcPassword);

        api = new MovieApiClient();
        movieConverter = new JsonToMovie();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();
        try {
            switch (action) {
                case "/new" -> showNewForm(request, response);
                case"/insert" -> insertMovie(request, response);
                case"/details" -> showMovieDetails(request,response);
                case"/update" -> updateMovie(request, response);
                case"/edit" -> showEditForm(request, response);
                case"/delete" -> deleteMovie(request,response);
                case"/search" -> searchMovie(request,response);
                default -> listAllMovies(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("errormessage", e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("Error.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void searchMovie(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException {
        String title = request.getParameter("title").trim();
        Movie movie = movieDao.getMovieByTitle(title);
        if (movie.getMovieId() == 0) {
             throw new ServletException("No movie found for: " + title);
        } else {
            request.setAttribute("movie", movie);
            request.getRequestDispatcher("Search.jsp").forward(request,response);
        }
    }

    private void listAllMovies(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        List<Movie> movieCollection = movieDao.listAllMovies();
        request.setAttribute("movies", movieCollection);
        RequestDispatcher dispatcher = request.getRequestDispatcher("Collection.jsp");
        dispatcher.forward(request,response);
    }
    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("AddMovie.jsp");
        dispatcher.forward(request,response);
    }

    public void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int movieId = Integer.parseInt(request.getParameter("movieId"));
        Movie movie = movieDao.getMovieById(movieId);
        List<String> allGenres = movieDao.listAllGenres();

        request.setAttribute("movie", movie);
        request.setAttribute("allGenres", allGenres);

        RequestDispatcher dispatcher = request.getRequestDispatcher("EditMovie.jsp");
        dispatcher.forward(request, response);
    }
    private void insertMovie(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String title = request.getParameter("title");
        String format = request.getParameter("format");
        int releaseYear = 0;
        Movie movie;

         try {
            releaseYear = Integer.parseInt((request.getParameter("releaseYear")));
            movie = movieConverter.movieAssembler((api.fetchMovie(title, releaseYear)), format);
         } catch (NumberFormatException e) {
            movie = movieConverter.movieAssembler(api.fetchMovie(title), format);
        }
        movieDao.insertMovie(movie);
        response.sendRedirect("list");
    }

    private void showMovieDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        int movieId = Integer.parseInt(request.getParameter("movieId"));
        Movie movie = movieDao.getMovieById(movieId);
        request.setAttribute("movie", movie);
        RequestDispatcher dispatcher = request.getRequestDispatcher("Details.jsp");
        dispatcher.forward(request,response);
    }

    private void updateMovie(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        int movieId = Integer.parseInt(request.getParameter("movieId"));
        String title = request.getParameter("title");
        String format = request.getParameter("format");
        String[] genres = request.getParameterValues("genres");
        String description = request.getParameter("description");

        Movie movie = movieDao.getMovieById(movieId);
        movie.setTitle(title);
        movie.setFormat(Formats.valueOf(format));
        movie.setGenreList(Arrays.stream(genres).toList());
        movie.setDescription(description);
        movieDao.updateMovie(movie);

        response.sendRedirect("list");
    }

    private void deleteMovie(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        int movieId = Integer.parseInt(request.getParameter("movieId"));
        Movie movie = movieDao.getMovieById(movieId);
        movieDao.deleteMovie(movie);
        response.sendRedirect("list");
    }
}