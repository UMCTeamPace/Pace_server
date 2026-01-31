package com.example.pace.domain.schedule.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@NoArgsConstructor
public class GoogleDirectionApiResponse {

    private List<Route> routes;

    @Getter
    public static class Route {
        private List<Leg> legs;
    }

    @Getter
    public static class Leg {
        private ArrivalTime arrivalTime;
        private DepartureTime departureTime;
        private Distance distance;
        private Duration duration;
        private List<Step> steps;

    }

    @Getter
    public static class Step {
        private Distance distance;
        private Duration duration;

        @JsonProperty("polyline")
        private EncodedPolyline encodedPolyline;

        private EndLocation endLocation;
        private StartLocation startLocation;
        private String travelMode;
        private List<Step> steps;
        private String htmlInstructions; // 안내문구
        private TransitDetails transitDetails;
    }


    @Getter
    public static class TransitDetails {
        private ArrivalStop arrivalStop;
        private ArrivalTime arrivalTime; //필요없어보이긴 한데 일단 넣어달라고 요청하심
        private DepartureTime departureTime;
        private DepartureStop departureStop;
        private String headsign; // ~~행 (버스기준? 지하철 적용이 힘듦)

        @JsonProperty("line")
        private EncodedLine encodedLine;

        private Long numStops;

    }

    @Getter
    public static class Duration {
        private String text;
        private Long value; // seconds
    }

    @Getter
    public static class Distance {
        private String text;
        private Long value;
    }

    @Getter
    public static class ArrivalStop {

        @JsonProperty("location")
        private EncodedLocation encodedLocation;

        @JsonProperty("name")
        private String encodedName;
    }

    @Getter
    public static class DepartureStop {
        private EncodedLocation encodedLocation;
        private String encodedName;
    }

    @Getter
    public static class EncodedLocation {
        private Double lat;
        private Double lng;
    }


    @Getter
    public static class Vehicle {

        @JsonProperty("name")
        private String vehicleName; //버스, 지하철 등
        private String type; //둘 중 하나만쓸까용? 일단 둘 다 넣어둠
    }


    @Getter
    public static class EncodedLine {
        private String color;
        private Vehicle vehicle;
        private String name; // 지하철 역 호선 이름
    }

    @Getter
    public static class DepartureTime {
        private String text;
        private Long value;
    }

    @Getter
    public static class ArrivalTime {
        private String text;
        private Long value;
    }


    @Getter
    //자바 내부에 폴리라인 임포트문이 있어서 이름 바꿨습니당
    public static class EncodedPolyline {
        private String points;
    }

    @Getter
    public static class StartLocation {
        private Double lat;
        private Double lng;
    }

    @Getter
    public static class EndLocation {
        private Double lat;
        private Double lng;
    }


}
