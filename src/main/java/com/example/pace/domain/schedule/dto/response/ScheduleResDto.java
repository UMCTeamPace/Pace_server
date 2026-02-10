package com.example.pace.domain.schedule.dto.response;

import com.example.pace.domain.schedule.enums.ReminderType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleResDto {
    private Long scheduleId;
    private ScheduleInfoDto scheduleInfo;
    private PlaceDto place;
    private List<ReminderDto> reminders;
    private RouteDto route;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ScheduleInfoDto {
        private String title;
        private Boolean isAllDay;
        private LocalDate startDate;
        private LocalDate endDate;
        private LocalTime startTime;
        private LocalTime endTime;
        private String memo;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlaceDto {
        private String targetName;
        private BigDecimal targetLat;
        private BigDecimal targetLng;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReminderDto {
        private ReminderType reminderType;
        private Integer minutesBefore;
        private Boolean reminderEnabled;
    }

    @Getter
    @Builder
    @AllArgsConstructor
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
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RouteDetailDto {
        private Integer sequence;
        private String description;
        private String transitType;
        private Integer duration;
        private Integer distance;
        private String lineName;
        private String lineColor;
        private String departureStop;
        private String arrivalStop;
        private BigDecimal startLat;
        private BigDecimal startLng;
        private BigDecimal endLat;
        private BigDecimal endLng;
        private String shortName;
    }
}
