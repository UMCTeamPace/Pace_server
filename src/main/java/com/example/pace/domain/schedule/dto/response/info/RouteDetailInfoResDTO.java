package com.example.pace.domain.schedule.dto.response.info;

import com.example.pace.domain.schedule.infrastructure.dto.GoogleDirectionApiResponse.Distance;
import com.example.pace.domain.schedule.infrastructure.dto.GoogleDirectionApiResponse.Duration;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor

public class RouteDetailInfoResDTO {

    private BigDecimal startLat;
    private BigDecimal startLng;
    private BigDecimal endLat;
    private BigDecimal endLng;

    private Integer sequence;
    private Integer duration;
    private Integer distance;

    private String description;
    private String points; //polyline

    // 교통수단일 경우만 채워짐 (service)
    private TransitRouteDetailInfoResDTO transitDetail;
}