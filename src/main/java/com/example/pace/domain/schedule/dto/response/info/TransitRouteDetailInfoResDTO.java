package com.example.pace.domain.schedule.dto.response.info;

import com.example.pace.domain.schedule.enums.TransitType;
import com.example.pace.domain.schedule.infrastructure.dto.GoogleDirectionApiResponse.ArrivalTime;
import com.example.pace.domain.schedule.infrastructure.dto.GoogleDirectionApiResponse.DepartureTime;
import com.example.pace.domain.schedule.infrastructure.dto.GoogleDirectionApiResponse.EncodedLine;
import com.example.pace.domain.schedule.infrastructure.dto.GoogleDirectionApiResponse.EncodedLocation;
import com.example.pace.domain.schedule.infrastructure.dto.GoogleDirectionApiResponse.EncodedPolyline;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class TransitRouteDetailInfoResDTO {

    //erd 기준
    private TransitType transitType;

    private String lineName;
    private String lineColor;
    private Integer stopCount;

    private String departureStop;  //encodedName
    private String arrivalStop; //encodedName


    // 얜 erd에 없음
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    private String shortName;
    private BigDecimal locationLat;
    private BigDecimal locationLng;


    private String headsign;
    private List<String> stationPath;
}
