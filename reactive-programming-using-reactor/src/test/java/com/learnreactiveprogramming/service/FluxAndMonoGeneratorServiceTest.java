package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

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
                .expectNext("5-SAYEN", "7-MARCELO")
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

    @Test
    void nameMono_map() {
        // given

        // when
        var nameMono = fluxAndMonoGeneratorService.nameMono_map();

        // then
        StepVerifier.create(nameMono)
                .expectNext("MARCELO")
                .verifyComplete();
    }

    @Test
    void nameMono_map_filter() {
        // given

        // when
        var nameMono = fluxAndMonoGeneratorService.nameMono_map_filter(3);

        // then
        StepVerifier.create(nameMono)
                .expectNext("MARCELO")
                .verifyComplete();
    }

    @Test
    void nameMono_flatMap() {
        // given

        // when
        var nameMono = fluxAndMonoGeneratorService.nameMono_flatMap(3);

        // then
        StepVerifier.create(nameMono)
                .expectNext(List.of("M","A","R","C","E","L","O"))
                .verifyComplete();
    }

    @Test
    void nameMono_flatMapMany() {
        // given

        // when
        var nameMono = fluxAndMonoGeneratorService.nameMono_flatMapMany(3);

        // then
        StepVerifier.create(nameMono)
                .expectNext("M","A","R","C","E","L","O")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform() {
        // given
        int stringLength = 3;

        // when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform(stringLength);

        // then
        StepVerifier.create(namesFlux)
                .expectNext("S", "A", "Y", "E", "N", "N", "A", "R", "C", "E", "L", "O")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform_1() {
        // given
        int stringLength = 10;

        // when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform(stringLength);

        // then
        StepVerifier.create(namesFlux)
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform__switchIfEmpty() {
        // given
        int stringLength = 10;

        // when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform_switchIfEmpty(stringLength);

        // then
        StepVerifier.create(namesFlux)
                .expectNext("D","E","F","A","U","L","T","I","F","E","M","P","T","Y")
                .verifyComplete();
    }

    @Test
    void explore_concat() {
        // given

        // when
        var concat = fluxAndMonoGeneratorService.explore_concat();

        // then
        StepVerifier.create(concat)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    void explore_concatWith() {
        // given

        // when
        var concat = fluxAndMonoGeneratorService.explore_concatWith();

        // then
        StepVerifier.create(concat)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    void explore_concatWith_mono() {
        // given

        // when
        var concat = fluxAndMonoGeneratorService.explore_concatWith_mono();

        // then
        StepVerifier.create(concat)
                .expectNext("A","D")
                .verifyComplete();
    }


    @Test
    void explore_merge() {
        // given

        // when
        var concat = fluxAndMonoGeneratorService.explore_merge();

        // then
        StepVerifier.create(concat)
                .expectNext("A","D","B","E","C","F")
                .verifyComplete();
    }

    @Test
    void explore_mergeWith() {
        // given

        // when
        var concat = fluxAndMonoGeneratorService.explore_mergeWith();

        // then
        StepVerifier.create(concat)
                .expectNext("A","D","B","E","C","F")
                .verifyComplete();
    }

    @Test
    void explore_mergeWithMono() {
        // given

        // when
        var concat = fluxAndMonoGeneratorService.explore_mergeWith_mono();

        // then
        StepVerifier.create(concat)
                .expectNext("A","D")
                .verifyComplete();
    }

    @Test
    void explore_mergeSequential() {
        // given

        // when
        var concat = fluxAndMonoGeneratorService.explore_mergeSequential();

        // then
        StepVerifier.create(concat)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    void explore_zip() {
        // given

        // when
        var concat = fluxAndMonoGeneratorService.explore_zip();

        // then
        StepVerifier.create(concat)
                .expectNext("AD","BE","CF")
                .verifyComplete();
    }

    @Test
    void explore_zip_1() {
        // given

        // when
        var concat = fluxAndMonoGeneratorService.explore_zip_1();

        // then
        StepVerifier.create(concat)
                .expectNext("AD14","BE25","CF36")
                .verifyComplete();
    }

    @Test
    void explore_zipWith() {
        // given

        // when
        var concat = fluxAndMonoGeneratorService.explore_zipWith();

        // then
        StepVerifier.create(concat)
                .expectNext("AD","BE","CF")
                .verifyComplete();
    }

    @Test
    void explore_zip_mono() {
        // given

        // when
        var concat = fluxAndMonoGeneratorService.explore_zipWith_mono();

        // then
        StepVerifier.create(concat)
                .expectNext("AD")
                .verifyComplete();
    }
}