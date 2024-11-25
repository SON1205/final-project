package com.ssafy.urs.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointDto {
    private int pointId;
    private BigDecimal latitude;
    private BigDecimal longitude;
}