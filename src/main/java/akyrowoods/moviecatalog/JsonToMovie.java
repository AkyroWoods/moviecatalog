package akyrowoods.moviecatalog;

import tools.jackson.databind.ObjectMapper;
import java.util.List;

public class JsonToMovie {
    private final ObjectMapper objectMapper;

    public JsonToMovie() {
        this.objectMapper = new ObjectMapper();
    }

    public Movie convertToMovie(String json)  {
        try {
            return  objectMapper.readValue(json, Movie.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
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
        List<String> genres = movie.buildGenreListFromCsv(movie.getGenreCsv());
        movie.setGenreList(genres);
        Formats format = sanitizeFormat(movieFormat);
        movie.setFormat(format);
        return movie;
    }

}
