package com.ssafy.urs.service;

import com.ssafy.urs.dto.LocationDto;
import com.ssafy.urs.repository.LocationRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Override
    public List<LocationDto> getAllLocationsByRoute(int routeId) {
        return locationRepository.findAllByRouteId(routeId).stream()
                .map(location -> new LocationDto(location.getId().getRouteId(), location.getId().getPointId(),
                        location.getType().name()))
                .collect(Collectors.toList());
    }
}
