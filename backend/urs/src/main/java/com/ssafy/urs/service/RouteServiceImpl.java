package com.ssafy.urs.service;

import com.ssafy.urs.dto.RouteDto;
import com.ssafy.urs.entity.Route;
import com.ssafy.urs.repository.RouteRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
    private final RouteRepository routeRepository;

    @Override
    public RouteDto getRouteById(int routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));
        return new RouteDto(route.getRouteId(), route.getDistrictName(), route.getThemeName(), route.getDurationTime(),
                route.getImageUrl(), route.getAverageRating());
    }

    @Override
    public List<RouteDto> getAllRoutes() {
        return routeRepository.findAll().stream()
                .map(route -> new RouteDto(route.getRouteId(), route.getDistrictName(), route.getThemeName(),
                        route.getDurationTime(), route.getImageUrl(), route.getAverageRating()))
                .collect(Collectors.toList());
    }

    @Override
    public RouteDto getRoutesByCriteria(String districtName, String themeName, int durationTime) {
        Route route = routeRepository.findByDistrictNameAndThemeNameAndDurationTimeLessThanEqual(districtName,
                themeName, durationTime);
        if (route == null) {
            throw new RuntimeException("No routes found matching the criteria");
        }
        return new RouteDto(route.getRouteId(), route.getDistrictName(), route.getThemeName(),
                route.getDurationTime(), route.getImageUrl(), route.getAverageRating());
    }
}
