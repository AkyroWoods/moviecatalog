package akyrowoods.moviecatalog;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;

public class Movie {
    @JsonProperty("Title")
    private String title; //user required

    private int movieId; //database generated
    //api call
    @JsonProperty("Year")

    private int releaseYear;
    @JsonProperty("Runtime")

    private int runtime;
    @JsonProperty("Director")
    private String director;

    @JsonProperty("Rated")
    private String rating;

    @JsonProperty("Genre")
    private String genreCsv;

    private List<String> genreList;
    @JsonProperty("Plot")
    private String description;

    private Formats format;

    @JsonProperty("Poster")
    private String posterUrl;

    public Movie() {

    }
    public Movie (String title, Formats format) {
        this.title = title;
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

    public String getGenreCsv() {
        return genreCsv;
    }

    public void setGenreCsv(String genreCsv) {this.genreCsv = genreCsv;}

    public List<String> buildGenreListFromCsv(String genreCsv) {
    if (genreCsv == null || genreCsv.isBlank())
    throw new NullPointerException("Movie not found: " + getTitle());

        return (Arrays.stream(genreCsv.split(","))
                .map(String::trim)
                .toList());
    }

    public List<String> getGenreList() {
        return genreList;
    }
    public void setGenreList(List<String> genres) {
        this.genreList = genres;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Formats getFormat() {
        return format;
    }
    public void setFormat(Formats format) {
        this.format = format;
    }

    public String getPosterUrl() {
        return posterUrl;
    }
    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", movieId=" + movieId +
                ", releaseYear=" + releaseYear +
                ", runtime=" + runtime +
                ", director='" + director + '\'' +
                ", rating='" + rating + '\'' +
                ", genreCsv='" + genreCsv + '\'' +
                ", genreList=" + genreList +
                ", description='" + description + '\'' +
                ", format=" + format +
                ", posterUrl='" + posterUrl + '\'' +
                '}';
    }
}
