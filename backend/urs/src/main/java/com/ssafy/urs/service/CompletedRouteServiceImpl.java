package com.ssafy.urs.service;

import com.ssafy.urs.dto.CompletedRouteDto;
import com.ssafy.urs.entity.CompletedRoute;
import com.ssafy.urs.entity.CompletedRouteId;
import com.ssafy.urs.repository.CompletedRouteRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompletedRouteServiceImpl implements CompletedRouteService {
    private final CompletedRouteRepository completedRouteRepository;

    @Override
    public CompletedRouteDto getCompletedRouteById(String userId, int routeId) {
        CompletedRoute completedRoute = completedRouteRepository.findById(new CompletedRouteId(userId, routeId))
                .orElseThrow(() -> new RuntimeException("Completed route not found"));
        return new CompletedRouteDto(completedRoute.getId().getUserId(), completedRoute.getId().getRouteId(),
                completedRoute.getMiddleImgUrl());
    }

    @Override
    public List<CompletedRouteDto> getAllCompletedRoutesByUser(String userId) {
        return completedRouteRepository.findAllByUserId(userId).stream()
                .map(completedRoute -> new CompletedRouteDto(completedRoute.getId().getUserId(),
                        completedRoute.getId().getRouteId(), completedRoute.getMiddleImgUrl()))
                .collect(Collectors.toList());
    }

    @Override
    public CompletedRouteDto createCompletedRoute(CompletedRouteDto completedRouteDto) {
        CompletedRoute completedRoute = new CompletedRoute(
                new CompletedRouteId(completedRouteDto.getUserId(), completedRouteDto.getRouteId()),
                completedRouteDto.getMiddleImgUrl());
        completedRouteRepository.save(completedRoute);
        return completedRouteDto;
    }

    @Override
    public void deleteCompletedRoute(String userId, int routeId) {
        completedRouteRepository.deleteById(new CompletedRouteId(userId, routeId));
    }
}