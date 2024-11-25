package com.ssafy.urs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Route")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private int routeId;

    @Column(name = "district_name", nullable = false)
    private String districtName;

    @Column(name = "theme_name", nullable = false)
    private String themeName;

    @Column(name = "duration_time", nullable = false)
    private int durationTime;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "average_rating", precision = 3, scale = 2)
    private BigDecimal averageRating;
}
