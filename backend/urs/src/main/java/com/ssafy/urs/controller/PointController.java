package com.ssafy.urs.controller;

import com.ssafy.urs.dto.PointDto;
import com.ssafy.urs.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PointController {
    private final PointService pointService;

    @GetMapping("/{pointId}")
    public ResponseEntity<PointDto> getPointById(@PathVariable int pointId) {
        try {
            return ResponseEntity.ok(pointService.getPointById(pointId));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}