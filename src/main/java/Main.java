import akyrowoods.moviecatalog.JsonToMovie;
import akyrowoods.moviecatalog.Movie;
import akyrowoods.moviecatalog.MovieApiClient;
import akyrowoods.moviecatalog.MovieDao;

void main() {
    MovieApiClient client = new MovieApiClient();
    JsonToMovie parser = new JsonToMovie();

  Movie movie = parser.movieAssembler(client.fetchMovie("Like+Mike"), "VHS");


    MovieDao login = new MovieDao("jdbc:postgresql:dvds", "postgres", "postgres");



}
