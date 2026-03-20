package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {
        // given

        // when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux();

        // then
        StepVerifier.create(namesFlux)
                //.expectNext("Leo", "Sayen", "Narcelo")
                //.expectNextCount(3)
                .expectNext("Leo")
                .expectNextCount(2)
                .verifyComplete();

    }

    @Test
    void nameMono() {
        // Given

        // When
        var nameMono = fluxAndMonoGeneratorService.nameMono();

        // Them
        StepVerifier.create(nameMono)
                .expectNext("Marcelo")
                //.expectNextCount(1)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void namesFlux_map() {
        // given

        // when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_map();

        // then
        StepVerifier.create(namesFlux)
                .expectNext("LEO", "SAYEN", "NARCELO")
                .verifyComplete();

    }

    @Test
    void namesFlux_immutability() {
        // given

        // when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_immutability();

        // then
        StepVerifier.create(namesFlux)
                // .expectNext("LEO","SAYEN","NARCELO")
                .expectNext("Leo", "Sayen", "Narcelo")
                .verifyComplete();
    }

    @Test
    void testNamesFlux_map() {
        // given
        int stringLength = 3;

        // when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_map(stringLength);

        // then
        StepVerifier.create(namesFlux)
                .expectNext("5-SAYEN", "7-NARCELO")
                .verifyComplete();

    }

    @Test
    void namesFlux_flatmap() {
        // given
        int stringLength = 3;

        // when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_flatmap(stringLength);

        // then
        StepVerifier.create(namesFlux)
                .expectNext("S", "A", "Y", "E", "N", "N", "A", "R", "C", "E", "L", "O")
                .verifyComplete();

    }

    @Test
    void namesFlux_flatmap_async() {
        // given
        int stringLength = 3;

        // when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_flatmap_async(stringLength);

        // then
        StepVerifier.create(namesFlux)
                //.expectNext("S","A","Y","E","N","N","A","R","C","E","L","O")
                .expectNextCount(12)
                .verifyComplete();

    }

    @Test
    void namesFlux_concatmap() {
        // given
        int stringLength = 3;

        // when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_concatmap(stringLength);

        // then
        StepVerifier.create(namesFlux)
                .expectNext("S","A","Y","E","N","N","A","R","C","E","L","O")
                .verifyComplete();

    }
}