package com.ssafy.urs.controller;

import com.ssafy.urs.dto.CompletedRouteDto;
import com.ssafy.urs.service.CompletedRouteService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/completed-routes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CompletedRouteController {
    private final CompletedRouteService completedRouteService;

    @GetMapping("/{userId}/{routeId}")
    public ResponseEntity<CompletedRouteDto> getCompletedRouteById(@PathVariable String userId,
                                                                   @PathVariable int routeId) {
        try {
            return ResponseEntity.ok(completedRouteService.getCompletedRouteById(userId, routeId));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CompletedRouteDto>> getAllCompletedRoutesByUser(@PathVariable String userId) {
        return ResponseEntity.ok(completedRouteService.getAllCompletedRoutesByUser(userId));
    }

    @Transactional
    @PostMapping
    public ResponseEntity<CompletedRouteDto> createCompletedRoute(@RequestBody CompletedRouteDto completedRouteDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(completedRouteService.createCompletedRoute(completedRouteDto));
    }

    @Transactional
    @DeleteMapping("/{userId}/{routeId}")
    public ResponseEntity<Void> deleteCompletedRoute(@PathVariable String userId, @PathVariable int routeId) {
        try {
            completedRouteService.deleteCompletedRoute(userId, routeId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}