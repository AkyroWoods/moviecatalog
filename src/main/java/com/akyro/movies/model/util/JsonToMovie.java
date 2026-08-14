package com.akyro.movies.model.util;

import com.akyro.movies.model.Movie;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import java.util.List;

public class JsonToMovie {
    private final ObjectMapper objectMapper;

    public JsonToMovie() {
        this.objectMapper = new ObjectMapper();
    }

    public Movie convertToMovie(String json)  {
        try {
            return objectMapper.readValue(json, Movie.class);
        } catch (JacksonException e) {
            throw new RuntimeException("Could not deserialize movie", e);
        }
    }

    public String sanitizeMovieTitle(String title) {
        return title.replace(" ", "+".trim());
    }

    public Movie movieAssembler(String json, String movieFormat) {
        Movie movie = convertToMovie(json);
        List<String> genres = movie.buildGenreListFromCsv(movie.getGenreCsv());
        movie.setGenreList(genres);
        movie.setFormat(Formats.valueOf(movieFormat));
        return movie;
    }

}