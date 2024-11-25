package com.ssafy.urs.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private int reviewId;
    private String userId;
    private int routeId;
    private LocalDateTime regDate;
    private int rating;
}
