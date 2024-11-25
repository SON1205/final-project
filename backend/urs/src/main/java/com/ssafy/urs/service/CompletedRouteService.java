package com.ssafy.urs.service;

import com.ssafy.urs.dto.CompletedRouteDto;
import java.util.List;

public interface CompletedRouteService {
    CompletedRouteDto getCompletedRouteById(String userId, int routeId);

    List<CompletedRouteDto> getAllCompletedRoutesByUser(String userId);

    CompletedRouteDto createCompletedRoute(CompletedRouteDto completedRouteDto);

    void deleteCompletedRoute(String userId, int routeId);
}
