package com.reactivespring.repository;

import com.reactivespring.domain.MovieInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataMongoTest
@ActiveProfiles("test")
class MovieInfoRepositoryIntgTest {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @BeforeEach
    void setUp() {
        var movieinfos = List.of(new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        movieInfoRepository.saveAll(movieinfos)
                .blockLast();
    }

    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll().block();
    }

    @Test
    void findAll() {
        // given

        // when
        var moviesInfoFlux = movieInfoRepository.findAll().log();


        // then
        StepVerifier.create(moviesInfoFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void findById() {
        // given

        // when
        var moviesInfoMono = movieInfoRepository.findById("abc").log();


        // then
        StepVerifier.create(moviesInfoMono)
                .assertNext(movieInfo ->
                        assertEquals("Dark Knight Rises", movieInfo.getName()))
                .verifyComplete();
    }

    @Test
    void save() {
        // given
        var movieInfo = new MovieInfo(null, "Batman Begins II",
                2015, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2015-06-15"));

        // when
        var moviesInfoMono = movieInfoRepository.save(movieInfo).log();


        // then
        StepVerifier.create(moviesInfoMono)
                .assertNext(info -> {
                    assertNotNull(info.getMovieInfoId());
                    assertEquals("Batman Begins II", info.getName());
                })
                .verifyComplete();
    }

    @Test
    void movieInfoUpdate() {
        // given
        var movieInfo = movieInfoRepository.findById("abc").block();
        movieInfo.setYear(2026);

        // when
        var moviesInfoMono = movieInfoRepository.save(movieInfo).log();


        // then
        StepVerifier.create(moviesInfoMono)
                .assertNext(info -> {
                    assertEquals(2026, info.getYear());
                })
                .verifyComplete();
    }

    @Test
    void deleteMovieInfo() {
        // given

        // when
        movieInfoRepository.deleteById("abc").block();

        var moviesInfoFlux = movieInfoRepository.findAll().log();

        // then
        StepVerifier.create(moviesInfoFlux)
                .expectNextCount(2)
                .verifyComplete();
    }
}