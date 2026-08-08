package akyrowoods.moviecatalog;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    private String title; //user required

    private int movieId; //database generated

    //api call
    private int releaseYear;
    private int runtime;
    private String director;
    private String rating;
    private List<String> genre;
    private String description;
    private String format;

    public Movie (String title) {
        this.title = title;
    }
    public Movie (int movieId, String title, int releaseYear, int runTime, String director,
                  String rating, List<String> genre, String description, String format) {
        this.movieId = movieId;
        this.title = title;
        this.releaseYear = releaseYear;
        this.runtime = runTime;
        this.director = director;
        this.rating = rating;
        this.genre = genre;
        this.description = description;
        this.format = format;
    }

    public int getMovieId() {return movieId;}

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
