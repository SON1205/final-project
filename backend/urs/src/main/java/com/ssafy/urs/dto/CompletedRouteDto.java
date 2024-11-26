package com.ssafy.urs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompletedRouteDto {
    private String userId;
    private int routeId;
    private String middleImgUrl;
}
