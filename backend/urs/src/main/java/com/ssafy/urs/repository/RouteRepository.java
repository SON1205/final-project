package com.ssafy.urs.repository;

import com.ssafy.urs.entity.Route;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route, Integer> {
    Route findByDistrictNameAndThemeNameAndDurationTimeLessThanEqual(String districtName, String themeName,
                                                                     int durationTime);

    List<Route> findTop10ByOrderByAverageRatingDesc();
}
