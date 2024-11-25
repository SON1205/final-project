package com.ssafy.urs.service;

import com.ssafy.urs.dto.PointDto;
import com.ssafy.urs.entity.Point;
import com.ssafy.urs.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
    private final PointRepository pointRepository;

    @Override
    public PointDto getPointById(int pointId) {
        Point point = pointRepository.findById(pointId)
                .orElseThrow(() -> new RuntimeException("Point not found"));
        return new PointDto(point.getPointId(), point.getLatitude(), point.getLongitude());
    }
}