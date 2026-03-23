package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

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
        return Flux.fromIterable(List.of("Leo", "Sayen", "Marcelo"))
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

    public Flux<String> namesFlux_transform(int stringLength) {

        Function<Flux<String>, Flux<String>> filterMap = name ->
                name.map(String::toUpperCase)
                        .filter(s -> s.length() > stringLength);

        return Flux.fromIterable(List.of("Leo", "Sayen", "Narcelo"))
                .transform(filterMap)
                .flatMap(s -> splitString(s))
                .defaultIfEmpty("default")
                .log();  // comes from db or service API call
    }

    public Flux<String> namesFlux_transform_switchIfEmpty(int stringLength) {

        Function<Flux<String>, Flux<String>> filterMap = name ->
                name.map(String::toUpperCase)
                        .filter(s -> s.length() > stringLength)
                        .flatMap(s -> splitString(s));

        var defaultFlux = Flux.just("defaultIfEmpty")
                .transform(filterMap);

        return Flux.fromIterable(List.of("Leo", "Sayen", "Narcelo"))
                .transform(filterMap)
                .switchIfEmpty(defaultFlux)
                .log();  // comes from db or service API call
    }

    public Flux<String> explore_concat(){
        var abcFlux = Flux.just("A","B","C");
        var edfFlux = Flux.just("D","E","F");

        return Flux.concat(abcFlux, edfFlux);
    }

    public Flux<String> explore_concatWith(){
        var abcFlux = Flux.just("A","B","C");
        var edfFlux = Flux.just("D","E","F");

        return abcFlux.concatWith(edfFlux);
    }

    public Flux<String> explore_concatWith_mono(){
        var aMono = Mono.just("A");
        var dMono = Mono.just("D");

        return aMono.concatWith(dMono);
    }


    public Flux<String> explore_merge(){
        var abcFlux = Flux.just("A","B","C")
                .delayElements(Duration.ofMillis(100));
        var edfFlux = Flux.just("D","E","F")
                .delayElements(Duration.ofMillis(125));;

        return Flux.merge(abcFlux, edfFlux).log();
    }

    public Flux<String> explore_mergeWith(){
        var abcFlux = Flux.just("A","B","C")
                .delayElements(Duration.ofMillis(100));
        var edfFlux = Flux.just("D","E","F")
                .delayElements(Duration.ofMillis(125));;

        return abcFlux.mergeWith(edfFlux).log();
    }

    public Flux<String> explore_mergeWith_mono(){
        var aMono = Mono.just("A");
        var dMono = Mono.just("D");

        return aMono.mergeWith(dMono).log();
    }

    public Flux<String> explore_mergeSequential(){
        var abcFlux = Flux.just("A","B","C")
                .delayElements(Duration.ofMillis(100));
        var edfFlux = Flux.just("D","E","F")
                .delayElements(Duration.ofMillis(125));;

        return  Flux.mergeSequential(abcFlux, edfFlux).log();
    }

    public Flux<String> explore_zip(){
        var abcFlux = Flux.just("A","B","C");
        var edfFlux = Flux.just("D","E","F");

        return  Flux.zip(abcFlux, edfFlux, (first, second) -> first + second ).log();
    }

    public Flux<String> explore_zip_1(){
        var abcFlux = Flux.just("A","B","C");
        var edfFlux = Flux.just("D","E","F");
        var _123Flux = Flux.just("1","2","3");
        var _456Flux = Flux.just("4","5","6");

        return  Flux.zip(abcFlux, edfFlux, _123Flux, _456Flux )
                .map(t4 -> t4.getT1() + t4.getT2() + t4.getT3() + t4.getT4())
                .log();
    }

    public Flux<String> explore_zipWith(){
        var abcFlux = Flux.just("A","B","C");
        var edfFlux = Flux.just("D","E","F");

        return  abcFlux.zipWith(edfFlux, (first, second) -> first + second ).log();
    }

    public Mono<String> explore_zipWith_mono(){
        var aMono = Mono.just("A");
        var dMono = Mono.just("D");

        return aMono.zipWith(dMono)
                .map(t2 -> t2.getT1() + t2.getT2())
                .log();
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

    public Mono<String> nameMono_map() {
        return Mono.just("Marcelo")
                .map(String::toUpperCase)
                .log();
    }

    public Mono<String> nameMono_map_filter(int stringLength) {
        return Mono.just("Marcelo")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .log();
    }

    public Mono<List<String>> nameMono_flatMap(int stringLength) {
        return Mono.just("Marcelo")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitStringMono)
                .log();
    }

    public Flux<String> nameMono_flatMapMany(int stringLength) {
        return Mono.just("Marcelo")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMapMany(this::splitString)
                .log();
    }

    private Mono<List<String>> splitStringMono(String s) {
        var charArray = s.split("");
        return Mono.just(List.of(charArray));
    }

    public static void main(String[] args) {
        var fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

        System.out.println("Flux response:");
        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(name -> System.out.println("Name is: " + name));

        System.out.println("Mono response:");
        fluxAndMonoGeneratorService.nameMono()
                .subscribe(name ->
                        System.out.println("Mono name is:" + name));
    }
}
