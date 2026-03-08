package com.example.pace.domain.schedule.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private String calendarId;
    private String color;
    private String memo;
    private Boolean isPathIncluded;
    private PlaceDto place;
    private List<ReminderDto> reminders;
    private RouteReqDto route;


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
    public static class RouteReqDto {
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
        private List<RouteDetailReqDto> routeDetails;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @Schema(name = "ScheduleReqRouteDetailDto")
    public static class RouteDetailReqDto {
        private Integer sequence;
        private Double startLat;
        private Double startLng;
        private Double endLat;
        private Double endLng;
        private Integer duration;
        private Integer distance;
        private String description;
        private String points;
        private TransitDetailDto transitDetail;
    }

}