

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

class ApiTest {
    @Test
    void fetchDonnieDarkoByTitle() {
        given()
                .baseUri("http://www.omdbapi.com")
                .queryParam("apikey", "3c4dc7f5")
                .queryParam("t", "Donnie Darko")
                .get("/")
                .then()
                .statusCode(200)
                .body("Title", equalTo("Donnie Darko"));
    }

    @Test
    void fetchScarface1932UsingYear() {
        given()
                .baseUri("http://www.omdbapi.com")
                .queryParam("apikey", "3c4dc7f5")
                .queryParam("t", "Scarface")
                .queryParam("y", "1932")
                .get("/")
                .then()
                .statusCode(200)
                .body("Title", equalTo("Scarface"))
                .body("Year", equalTo("1932"));
    }

    @Test
    void fetchMovie_genresAreCommaSeparated() {
        given()
                .baseUri("http://www.omdbapi.com")
                .queryParam("apikey", "3c4dc7f5")
                .queryParam("t", "Donnie Darko")
                .get("/")
                .then()
                .body("Genre", containsString(","));
    }
    @Test
    void fetchMovie_withSpecialCharacters() {
        given()
                .baseUri("http://www.omdbapi.com")
                .queryParam("apikey", "3c4dc7f5")
                .queryParam("t", "Mr. Right")
                .get("/")
                .then()
                .statusCode(200)
                .body("Title", equalTo("Mr. Right"))
                .body("Response", equalTo("True"));
    }
    @Test
    void fetchNonexistentMovie_returnsError() {
        given()
                .baseUri("http://www.omdbapi.com")
                .queryParam("apikey", "3c4dc7f5")
                .queryParam("t", "RandomMovie123")
                .get("/")
                .then()
                .statusCode(200)
                .body("Response", equalTo("False"))
                .body("Error", containsString("Movie not found"));
    }


}