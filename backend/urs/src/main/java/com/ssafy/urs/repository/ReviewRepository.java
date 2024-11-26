package com.ssafy.urs.repository;

import com.ssafy.urs.entity.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findAllByRouteId(int routeId);

    List<Review> findAllByUserId(String userId);
}
