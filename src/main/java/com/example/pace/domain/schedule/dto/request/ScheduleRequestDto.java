package com.example.pace.domain.schedule.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ScheduleRequestDto {

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
    @NoArgsConstructor
    public static class PlaceDto {
        private String targetName;
        private Double targetLat;
        private Double targetLng;
    }

    @Getter
    @NoArgsConstructor
    public static class ReminderDto {
        private String reminderType;
        private Integer minutesBefore;
    }

    @Getter
    @NoArgsConstructor
    public static class RouteDto {
        private String originName;
        private Double originLat;
        private Double originLng;
        private String destName;
        private Double destLat;
        private Double destLng;
        private Integer totalTime;
        private Integer totalDistance;
        private List<RouteDetailDto> routeDetails;
    }

    @Getter
    @NoArgsConstructor
    public static class RouteDetailDto {
    }
}