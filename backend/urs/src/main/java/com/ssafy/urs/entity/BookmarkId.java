package com.ssafy.urs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkId {
    @Column(name = "user_id")
    private String userId;

    @Column(name = "route_id")
    private int routeId;
}