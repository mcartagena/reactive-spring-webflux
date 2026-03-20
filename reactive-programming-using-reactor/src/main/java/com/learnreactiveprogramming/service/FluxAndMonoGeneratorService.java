package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux() {
        return Flux.fromIterable(List.of("Leo", "Sayen", "Narcelo"))
                .log();  // comes from db or service API call
    }

    public Flux<String> namesFlux_map() {
        return Flux.fromIterable(List.of("Leo", "Sayen", "Narcelo"))
                .map(String::toUpperCase)
                .log();  // comes from db or service API call
    }

    public Flux<String> namesFlux_map(int stringLength) {
        return Flux.fromIterable(List.of("Leo", "Sayen", "Narcelo"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .map(s -> s.length() + "-" + s)
                .log();  // comes from db or service API call
    }

    public Flux<String> namesFlux_flatmap(int stringLength) {
        return Flux.fromIterable(List.of("Leo", "Sayen", "Narcelo"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(s -> splitString(s))
                .log();  // comes from db or service API call
    }

    public Flux<String> namesFlux_flatmap_async(int stringLength) {
        return Flux.fromIterable(List.of("Leo", "Sayen", "Narcelo"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(s -> splitString_withDelay(s))
                .log();  // comes from db or service API call
    }

    public Flux<String> namesFlux_concatmap(int stringLength) {
        return Flux.fromIterable(List.of("Leo", "Sayen", "Narcelo"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .concatMap(s -> splitString_withDelay(s))
                .log();  // comes from db or service API call
    }

    public Flux<String> splitString(String name) {
        var arrayString = name.split("");
        return Flux.fromArray(arrayString);
    }

    public Flux<String> splitString_withDelay(String name) {
        var arrayString = name.split("");
//        var delay = new Random().nextInt(1000);
        var delay = 1000;
        return Flux.fromArray(arrayString)
                .delayElements(Duration.ofMillis(delay));
    }

    public Flux<String> namesFlux_immutability() {
        var namesFlux = Flux.fromIterable(List.of("Leo", "Sayen", "Narcelo"));

        namesFlux.map(String::toUpperCase)
                .log();  // comes from db or service API call

        return namesFlux;
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
