package com.reactivespring.controller;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.service.MoviesInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest(controllers = MoviesInfoController.class)
@AutoConfigureWebTestClient
class MoviesInfoControllerUnitTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private MoviesInfoService moviesInfoServiceMock;

    final String MOVIES_INFO_URL = "/v1/movieinfos";

    @Test
    void getAllMovies() {

        var movieinfos = List.of(new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        when(moviesInfoServiceMock.getAllMovies()).thenReturn(Flux.fromIterable(movieinfos));

        webTestClient.get()
                .uri(MOVIES_INFO_URL)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);

    }

    @Test
    void getMoviesInfoById() {

        var id = "abc";
        var returnedMoviesInfo = new MovieInfo("abc", "Dark Knight Rises",
                2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"));

        when(moviesInfoServiceMock.getMoviesInfoById(id)).thenReturn(Mono.just(returnedMoviesInfo));

        webTestClient.get()
                .uri(MOVIES_INFO_URL + "/{id}", id)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
/*                .expectBody()
                .jsonPath("$.name").isEqualTo("Dark Knight Rises");*/
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var movieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assertNotNull(movieInfo);
                    assertEquals("abc", movieInfo.getMovieInfoId());
                });
    }

    @Test
    void addMovieInfo() {

        var movieInfoToAdd = new MovieInfo(null, "Batman Begins II",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        when(moviesInfoServiceMock.addMovieInfo(isA(MovieInfo.class)))
                .thenReturn(Mono.just(new MovieInfo("mockId", "Batman Begins II",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"))));

        webTestClient.post()
                .uri(MOVIES_INFO_URL)
                .bodyValue(movieInfoToAdd)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var saveMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assert saveMovieInfo != null;
                    assert saveMovieInfo.getMovieInfoId() != null;
                    assertEquals("mockId",saveMovieInfo.getMovieInfoId());
                });
    }

    @Test
    void addMovieInfo_validation() {

        var movieInfoToAdd = new MovieInfo(null, "",
                -2005, List.of(""), LocalDate.parse("2005-06-15"));

        webTestClient.post()
                .uri(MOVIES_INFO_URL)
                .bodyValue(movieInfoToAdd)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                    var responseBody = stringEntityExchangeResult.getResponseBody();
                    System.out.println("Response body: " + responseBody);
                    String expectedValue = "MovieInfo.cast must be present,MovieInfo.name must be present,MovieInfo.year must be a positve value";
                    assert responseBody != null;
                    assertEquals(expectedValue, responseBody);

                });
    }

    @Test
    void updateMoviesInfo() {

        var id = "abc";

        var movieToUpdate = new MovieInfo(null, "Dark Knight Rises II",
                2012, List.of("Christian Bale", "Tom Hardy", "Marcelo Cartagena"), LocalDate.parse("2026-05-05"));

        when(moviesInfoServiceMock.updateMovieInfo(isA(MovieInfo.class), isA(String.class)))
                .thenReturn(Mono.just(new MovieInfo("mockId", "Dark Knight Rises II",
                        2012, List.of("Christian Bale", "Tom Hardy", "Marcelo Cartagena"), LocalDate.parse("2026-05-05"))));

        webTestClient.put()
                .uri(MOVIES_INFO_URL + "/{id}", id)
                .bodyValue(movieToUpdate)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var movieInfoUpdated = movieInfoEntityExchangeResult.getResponseBody();
                    assertNotNull(movieInfoUpdated);
                    assertNotNull(movieInfoUpdated.getMovieInfoId());
                    assertEquals("Dark Knight Rises II", movieInfoUpdated.getName());
                    assertNotNull(movieInfoUpdated.getCast().contains("Marcelo Cartagena"));
                });
    }

    @Test
    void deleteMoviesInfo() {

        var id = "abc";

        when(moviesInfoServiceMock.deleteMovieInfo(id)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(MOVIES_INFO_URL + "/{id}", id)
                .exchange()
                .expectStatus()
                .isNoContent()
                .expectBody(Void.class);
    }

}