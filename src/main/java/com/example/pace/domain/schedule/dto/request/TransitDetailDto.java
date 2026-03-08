package com.example.pace.domain.schedule.dto.request;

import com.example.pace.domain.schedule.enums.TransitType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransitDetailDto {
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