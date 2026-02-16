package com.example.pace.domain.schedule.dto.request;

import com.example.pace.domain.schedule.enums.TransitType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleRouteUpdateReqDto {
    // RouteReqDto 구조와 동일하게 구성
    private String originName;
    private BigDecimal originLat;
    private BigDecimal originLng;
    private String destName;
    private BigDecimal destLat;
    private BigDecimal destLng;
    private Integer totalTime;
    private Integer totalDistance;
    private LocalDateTime arrivalTime;
    private LocalDateTime departureTime;
    private List<RouteDetailUpdateReqDto> routeDetails;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RouteDetailUpdateReqDto {
        private Integer sequence;
        private Integer duration;
        private Integer distance;
        private String description;
        private String points;
        private Double startLat;
        private Double startLng;
        private Double endLat;
        private Double endLng;
        private TransitDetailUpdateReqDto transitDetail;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TransitDetailUpdateReqDto {
        private TransitType transitType;
        private String lineName;
        private String lineColor;
        private Integer stopCount;
        private String departureStop;
        private String arrivalStop;
        private LocalDateTime departureTime;
        private LocalDateTime arrivalTime;
        private String shortName;
        private Double locationLat;
        private Double locationLng;
        private String headsign;
        private List<String> stationPath;
    }
}