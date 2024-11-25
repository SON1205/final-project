package com.ssafy.urs.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDto {
    private int routeId;
    private String districtName;
    private String themeName;
    private int durationTime;
    private String imageUrl;
    private BigDecimal averageRating;
}
