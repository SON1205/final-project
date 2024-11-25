package com.ssafy.urs.repository;

import com.ssafy.urs.entity.CompletedRoute;
import com.ssafy.urs.entity.CompletedRouteId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletedRouteRepository extends JpaRepository<CompletedRoute, CompletedRouteId> {
    // 사용자 ID로 북마크 조회하는 쿼리
    @Query("SELECT c FROM CompletedRoute c WHERE c.id.userId = :userId")
    List<CompletedRoute> findAllByUserId(@Param("userId") String userId);
}
