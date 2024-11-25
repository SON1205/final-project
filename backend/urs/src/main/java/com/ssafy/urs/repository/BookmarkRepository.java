package com.ssafy.urs.repository;

import com.ssafy.urs.entity.Bookmark;
import com.ssafy.urs.entity.BookmarkId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkId> {
    // 사용자 ID로 북마크 조회하는 쿼리
    @Query("SELECT b FROM Bookmark b WHERE b.id.userId = :userId")
    List<Bookmark> findAllByUserId(@Param("userId") String userId);
}