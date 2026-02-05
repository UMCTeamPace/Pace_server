package com.example.pace.domain.schedule.controller;

import com.example.pace.domain.schedule.dto.request.RouteSaveReqDto;
import com.example.pace.domain.schedule.dto.response.RouteApiResDto;
import com.example.pace.domain.schedule.dto.response.RouteListResDTO;
import com.example.pace.domain.schedule.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/routes") // URL 예시
public class RouteController {

    private final RouteService routeService;

    // 길찾기 검색 API
    // GET 요청
    @GetMapping("/search")
    public ResponseEntity<RouteListResDTO> searchRoute(@ModelAttribute RouteSaveReqDto.CreateRouteDTO request) {

        RouteListResDTO response = routeService.searchRoute(request);

        return ResponseEntity.ok(response);
    }
}