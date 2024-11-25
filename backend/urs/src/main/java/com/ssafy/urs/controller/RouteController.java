package com.ssafy.urs.controller;

import com.ssafy.urs.dto.RouteDto;
import com.ssafy.urs.entity.Route;
import com.ssafy.urs.repository.RouteRepository;
import com.ssafy.urs.service.RouteService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class RouteController {
    private final RouteService routeService;
    private final RouteRepository routeRepository;

    @GetMapping("/top")
    public ResponseEntity<List<RouteDto>> getTopRoutes() {
        List<Route> topRoutes = routeRepository.findTop10ByOrderByAverageRatingDesc();
        List<RouteDto> routeDtos = topRoutes.stream()
                .map(route -> new RouteDto(
                        route.getRouteId(),
                        route.getDistrictName(),
                        route.getThemeName(),
                        route.getDurationTime(),
                        route.getImageUrl(),
                        route.getAverageRating()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(routeDtos);
    }

    @GetMapping("/{routeId}")
    public ResponseEntity<RouteDto> getRouteById(@PathVariable int routeId) {
        try {
            for (RouteDto allRoute : routeService.getAllRoutes()) {
                System.out.println(allRoute);
            }

            return ResponseEntity.ok(routeService.getRouteById(routeId));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<RouteDto>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    @GetMapping("/search")
    public ResponseEntity<RouteDto> getRoutesByCriteria(@RequestParam String district, @RequestParam String theme,
                                                        @RequestParam int duration) {
        try {
            System.out.println(district);
            return ResponseEntity.ok(routeService.getRoutesByCriteria(district, theme, duration));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}