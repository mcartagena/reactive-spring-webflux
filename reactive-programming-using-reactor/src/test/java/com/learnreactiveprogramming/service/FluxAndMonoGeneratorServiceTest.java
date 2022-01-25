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
}