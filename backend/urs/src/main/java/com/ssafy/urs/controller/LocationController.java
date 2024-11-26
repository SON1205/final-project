package com.ssafy.urs.controller;

import com.ssafy.urs.dto.LocationDto;
import com.ssafy.urs.service.LocationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class LocationController {
    private final LocationService locationService;

    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<LocationDto>> getAllLocationsByRoute(@PathVariable int routeId) {
        return ResponseEntity.ok(locationService.getAllLocationsByRoute(routeId));
    }
}
