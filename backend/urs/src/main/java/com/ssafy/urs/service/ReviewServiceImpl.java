package com.ssafy.urs.service;

import com.ssafy.urs.dto.ReviewDto;
import com.ssafy.urs.entity.Review;
import com.ssafy.urs.repository.ReviewRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    @Override
    public ReviewDto getReviewById(int reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        return new ReviewDto(review.getReviewId(), review.getUserId(), review.getRouteId(), review.getRegDate(),
                review.getRating());
    }

    @Override
    public List<ReviewDto> getAllReviewsByRoute(int routeId) {
        return reviewRepository.findAllByRouteId(routeId).stream()
                .map(review -> new ReviewDto(review.getReviewId(), review.getUserId(), review.getRouteId(),
                        review.getRegDate(), review.getRating()))
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDto createReview(ReviewDto reviewDto) {
        Review review = new Review(null, reviewDto.getUserId(), reviewDto.getRouteId(), reviewDto.getRegDate(),
                reviewDto.getRating());
        reviewRepository.save(review);
        return reviewDto;
    }
}
