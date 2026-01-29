package com.example.pace.domain.schedule.dto.request;

import com.example.pace.domain.schedule.enums.TransitType;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.example.pace.domain.schedule.enums.ReminderType;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleReqDto {

    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String memo;
    private Boolean isPathIncluded;
    private PlaceDto place;
    private List<ReminderDto> reminders;
    // 경로 정보 (나중에)
    private RouteDto route;


    @Getter
    @Setter
    @NoArgsConstructor
    public static class PlaceDto {
        private String targetName;
        private BigDecimal targetLat;
        private BigDecimal targetLng;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ReminderDto {
        private ReminderType reminderType;
        private Integer minutesBefore;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RouteDto {
        private String originName;
        private BigDecimal originLat;
        private BigDecimal originLng;
        private String destName;
        private BigDecimal destLat;
        private BigDecimal destLng;
        private Integer totalTime;
        private Integer totalDistance;
        private List<RouteDetailDto> routeDetails;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RouteDetailDto {
        private Integer sequence;
        private Integer duration;
        private Integer distance;
        private String description;
        private Double startLat;
        private Double startLng;
        private Double endLat;
        private Double endLng;
        private TransitType transitType;
        private String lineName;
        private String lineColor;
        private Integer stopCount;
        private String departureStop;
        private String arrivalStop;
    }
}