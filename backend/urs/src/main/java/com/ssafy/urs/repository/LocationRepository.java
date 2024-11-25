package com.ssafy.urs.repository;

import com.ssafy.urs.entity.Location;
import com.ssafy.urs.entity.LocationId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, LocationId> {
    @Query("SELECT l FROM Location l WHERE l.id.routeId = :routeId")
    List<Location> findAllByRouteId(@Param("routeId") int routeId);
}
