package com.reactivespring.routes;

import com.reactivespring.domain.Review;
import com.reactivespring.repository.ReviewReactiveRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@Testcontainers
public class ReviewsIntgTest {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ReviewReactiveRepository reviewReactiveRepository;

    final String REVIEWS_URL = "/v1/reviews";

    @BeforeEach
    void setUp() {
        var reviewsList = List.of(
                new Review("1", 1L, "Awesome Movie", 9.0),
                new Review(null, 1L, "Awesome Movie1", 9.0),
                new Review(null, 2L, "Excellent Movie", 8.0));
        reviewReactiveRepository.saveAll(reviewsList)
                .blockLast();
    }

    @AfterEach
    void tearDown() {
        reviewReactiveRepository.deleteAll().block();
    }

    @Test
    void addReview() {
        var review = new Review(null, 1L, "Awesome Movie", 9.0);

        webTestClient
                .post()
                .uri(REVIEWS_URL)
                .bodyValue(review)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Review.class)
                .consumeWith(reviewEntityExchangeResult -> {
                        var savedReview = reviewEntityExchangeResult.getResponseBody();
                        assert savedReview != null;
                        assert savedReview.getReviewId() != null;
                        });

    }

    @Test
    void getAllReviews() {

        webTestClient.get()
                .uri(REVIEWS_URL)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Review.class)
                .hasSize(3);
    }

    @Test
    void updateReview() {

        var id = "1";

        var reviewToUpdate = new Review(null, 1L, "Awesome Movie Updated", 8.5);

        webTestClient.put()
                .uri(REVIEWS_URL + "/{id}", id)
                .bodyValue(reviewToUpdate)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Review.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var reviewUpdated = movieInfoEntityExchangeResult.getResponseBody();
                    assertNotNull(reviewUpdated);
                    assertEquals("Awesome Movie Updated", reviewUpdated.getComment());
                    assertEquals(8.5, reviewUpdated.getRating());
                });
    }

    @Test
    void deleteReview() {

        var id = "1";

        webTestClient.delete()
                .uri(REVIEWS_URL + "/{id}", id)
                .exchange()
                .expectStatus()
                .isNoContent()
                .expectBody(Void.class);
    }

    @Test
    void getReviewByMovieInfoId() {

        var id = "1";

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(REVIEWS_URL)
                        .queryParam("movieInfoId", id)
                        .build()
                )
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Review.class)
                .hasSize(2);

    }
}
