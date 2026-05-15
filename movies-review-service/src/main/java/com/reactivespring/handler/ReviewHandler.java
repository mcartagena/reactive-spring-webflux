package com.reactivespring.handler;

import com.reactivespring.domain.Review;
import com.reactivespring.repository.ReviewReactiveRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ReviewHandler {

    private ReviewReactiveRepository reviewReactiveRepository;

    public ReviewHandler(ReviewReactiveRepository reviewReactiveRepository) {
        this.reviewReactiveRepository = reviewReactiveRepository;
    }

    public Mono<ServerResponse> addReview(ServerRequest request) {
        return request.bodyToMono(Review.class)
                .flatMap(review -> reviewReactiveRepository
                        .save(review))
                .flatMap(savedReview -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .bodyValue(savedReview));
    }

    public Mono<ServerResponse> getReviews(ServerRequest request) {

        var movieInfoId = request.queryParam("movieInfoId");

        if (movieInfoId.isPresent()) {
            return ServerResponse.ok()
                    .body(reviewReactiveRepository.findReviewsByMovieInfoId(
                            Long.valueOf(movieInfoId.get())), Review.class);
        } else {
            return ServerResponse.ok()
                    .body(reviewReactiveRepository.findAll(), Review.class);
        }

    }

    public Mono<ServerResponse> updateReview(ServerRequest request) {
        var reviewId = request.pathVariable("id");

        return reviewReactiveRepository.findById(reviewId)
                .flatMap(review ->
                        request.bodyToMono(Review.class)
                                .map(reqReview -> {
                                    review.setComment(reqReview.getComment());
                                    review.setRating(reqReview.getRating());
                                    return review;
                                })
                                .flatMap(reviewReactiveRepository::save)
                                .flatMap(saveReview -> ServerResponse.ok()
                                        .bodyValue(saveReview))
                );
    }

    public Mono<ServerResponse> deleteReview(ServerRequest request) {
        var reviewId = request.pathVariable("id");

        return reviewReactiveRepository.findById(reviewId)
                .flatMap(review -> reviewReactiveRepository.deleteById(reviewId))
                .then(ServerResponse.noContent().build());
    }
}
