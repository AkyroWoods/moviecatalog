package akyrowoods.moviecatalog;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonToMovie {
    private final Gson gson;

    public JsonToMovie() {
        this.gson = new Gson();
    }

    public Movie convertToMovie(String json) {
        return gson.fromJson(json, Movie.class);
    }

    public List<String> convertGenresToList(Movie movieData) {
        List<String> genreList = new ArrayList<>();
        String genres = movieData.getGenre();
        String[] separated = genres.split(",");

        Collections.addAll(genreList, separated);
        return genreList;
    }

    public Formats sanitizeFormat(String format) {
        String sanitizedFormat = format.trim()
                .replace("-", "_")
                .replace(" ", "_")
                .replace("4k", "UHD_4k")
                .toUpperCase();

        return Formats.valueOf(sanitizedFormat);

    }

    public Movie movieAssembler(String json, String movieFormat) {
        Movie movie = convertToMovie(json);
        List<String> genreList = convertGenresToList(movie);
        movie.setGenre(genreList);

        Formats format = sanitizeFormat(movieFormat);
        movie.setFormat(format);

        return movie;
    }

}
