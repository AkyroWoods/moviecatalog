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

}
