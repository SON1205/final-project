package com.ssafy.urs.service;

import com.ssafy.urs.dto.RouteDto;
import java.util.List;

public interface RouteService {
    RouteDto getRouteById(int routeId);

    List<RouteDto> getAllRoutes();

    RouteDto getRoutesByCriteria(String districtName, String themeName, int durationTime);
//    RouteDto createRoute(RouteDto routeDto);
//    RouteDto updateRoute(Long routeId, RouteDto routeDto);
//    void deleteRoute(Long routeId);
}
