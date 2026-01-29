package com.example.pace.domain.schedule.dto.request;

import com.example.pace.domain.schedule.enums.TransitType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleRouteUpdateReqDto {


    @JsonProperty("start_time")
    private String startTime;

    @JsonProperty("end_time")
    private String endTime;

    @JsonProperty("total_time")
    private Integer totalTime;

    @JsonProperty("total_distance")
    private Integer totalDistance;

    private OriginDto origin;
    private DestDto dest;

    private List<RouteDetailDto> routeDetails;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OriginDto {
        @JsonProperty("origin_name")
        private String originName;
        @JsonProperty("origin_lat")
        private Double originLat;
        @JsonProperty("origin_lng")
        private Double originLng;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DestDto {
        @JsonProperty("dest_name")
        private String destName;
        @JsonProperty("dest_lat")
        private Double destLat;
        @JsonProperty("dest_lng")
        private Double destLng;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RouteDetailDto {

        private Integer sequence;

        @JsonProperty("start_lat")
        private Double startLat;
        @JsonProperty("start_lng")
        private Double startLng;

        @JsonProperty("end_lat")
        private Double endLat;
        @JsonProperty("end_lng")
        private Double endLng;

        @JsonProperty("transit_type")
        private TransitType transitType;

        private Integer duration;
        private Integer distance;
        private String description;

        @JsonProperty("line_name")
        private String lineName;

        @JsonProperty("line_color")
        private String lineColor;

        @JsonProperty("stop_count")
        private Integer stopCount;

        @JsonProperty("departure_stop")
        private String departureStop;

        @JsonProperty("arrival_stop")
        private String arrivalStop;
    }
}