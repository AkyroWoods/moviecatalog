package com.akyro.movies.model.api;

import com.akyro.movies.model.util.JsonToMovie;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MovieApiClient {
    private final HttpClient client;
    private final JsonToMovie formatter;

    public MovieApiClient() {
        this.client = HttpClient.newHttpClient();
        this.formatter = new JsonToMovie();
    }

    public static String getOmdbApiKey() {
        try {
            return (String) new InitialContext().lookup("java:comp/env/omdbApiKey");
        } catch (NamingException e) {
            throw new RuntimeException("Missing JNDI API key: omdbApiKey", e);
        }
    }

    public String fetchMovie(String title) {
        String apiKey = getOmdbApiKey();
        String sanitizedTitle = formatter.sanitizeMovieTitle(title);
        String apiResponse = "";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://www.omdbapi.com/?apikey=" + apiKey + "&t=" + sanitizedTitle + "&plot=full"))
                .GET().build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            apiResponse = response.body().replace("min", "");
        } catch (Exception e) {
            throw new RuntimeException("Api Request Failed: ", e);
        }
        return apiResponse;
    }

    public String fetchMovie(String title, int releaseYear) {
        String apiKey = getOmdbApiKey();
        String sanitizedTitle = formatter.sanitizeMovieTitle(title);
        String apiResponse = "";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://www.omdbapi.com/?apikey=" + apiKey + "&t=" + sanitizedTitle + "&y=" + releaseYear + "&plot=full"))
                .GET().build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            apiResponse = response.body().replace("min", "");
        } catch (Exception e) {
            throw new RuntimeException("Api Request Failed: ", e);
        }
        return apiResponse;
    }
}