package akyrowoods.moviecatalog;

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

    public String fetchMovie(String title) {
        String sanitizedTitle = formatter.sanitizeMovieTitle(title);
        String apiResponse = "";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://www.omdbapi.com/?i=tt3896198&apikey=3c4dc7f5" + "&t=" + sanitizedTitle))
                .GET().build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            apiResponse = response.body().replace("min", "");
        } catch (Exception e) {
            System.out.println("Error Occurred: " + e.getMessage());

        }
        return apiResponse;
    }

    public String fetchMovie(String title, int releaseYear) {
        String sanitizedTitle = formatter.sanitizeMovieTitle(title);
        String apiResponse = "";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://www.omdbapi.com/?i=tt3896198&apikey=3c4dc7f5" + "&t=" + sanitizedTitle + "&Y=" + releaseYear))
                .GET().build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            apiResponse = response.body().replace("min", "");
        } catch (Exception e) {
            System.out.println("Error Occurred: " + e.getMessage());

        }
        return apiResponse;
    }


}
