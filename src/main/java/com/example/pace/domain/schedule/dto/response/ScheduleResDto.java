package com.example.pace.domain.schedule.dto.response;

import com.example.pace.domain.schedule.enums.ReminderType;
import com.example.pace.domain.schedule.enums.TransitType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private RouteResDto route;

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
    public static class RouteResDto {
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
        private List<RouteDetailResDto> routeDetails;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "ScheduleResRouteDetailDto")
    public static class RouteDetailResDto {
        private Integer sequence;
        private Double startLat;
        private Double startLng;
        private Double endLat;
        private Double endLng;
        private Integer duration;
        private Integer distance;
        private String description;
        private String points;
        private TransitDetailResDto transitDetail;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TransitDetailResDto {
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
