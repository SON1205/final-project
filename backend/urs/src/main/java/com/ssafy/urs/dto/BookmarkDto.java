package com.ssafy.urs.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkDto {
    private String userId;
    private int routeId;
    private LocalDateTime createdAt;
}
