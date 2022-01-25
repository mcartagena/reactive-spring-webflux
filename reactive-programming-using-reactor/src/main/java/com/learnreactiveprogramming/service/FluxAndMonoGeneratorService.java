package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux() {
        return Flux.fromIterable(List.of("Leo", "Sayen", "Narcelo"))
                .log();  // comes from db or service API call
    }

    public Mono<String> nameMono() {
        return Mono.just("Marcelo")
                .log();
    }

    public static void main(String[] args) {
        var fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

        System.out.println("Flux response:");
        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(name -> System.out.println(name));

        System.out.println("Mono response:");
        fluxAndMonoGeneratorService.nameMono()
                .subscribe(System.out::println);
    }
}
