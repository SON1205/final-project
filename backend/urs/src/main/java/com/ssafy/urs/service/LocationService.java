package com.ssafy.urs.service;

import com.ssafy.urs.dto.LocationDto;
import java.util.List;

public interface LocationService {
//    LocationDto getLocationById(Long routeId, int pointId);

    List<LocationDto> getAllLocationsByRoute(int routeId);
//    LocationDto createLocation(LocationDto locationDto);
//    void deleteLocation(Long routeId, Long pointId);
}
