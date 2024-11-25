package com.ssafy.urs.service;

import com.ssafy.urs.dto.ReviewDto;
import java.util.List;

public interface ReviewService {
    ReviewDto getReviewById(int reviewId);

    List<ReviewDto> getAllReviewsByRoute(int routeId);

    ReviewDto createReview(ReviewDto reviewDto);
//    ReviewDto updateReview(Long reviewId, ReviewDto reviewDto);
//    void deleteReview(Long reviewId);
}
