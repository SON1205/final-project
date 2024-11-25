package com.ssafy.urs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Bookmark")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bookmark {
    @EmbeddedId
    private BookmarkId id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
