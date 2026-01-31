package com.example.pace.domain.schedule.dto.response;

import com.example.pace.domain.schedule.dto.response.info.RouteDetailInfoResDTO;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

//경로 저장 DTO api->서버
@Getter
@Builder
@AllArgsConstructor
public class RouteApiResDto {
    private Integer totalDistance;
    private Integer totalTime;

    //도착시간
    private LocalDateTime arrivalTime;
    //출발시간
    private LocalDateTime departureTime;

    private List<RouteDetailInfoResDTO> routeDetailInfoResDTOList;
}
