package com.example.pace.domain.schedule.dto.response.info;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RouteDetailInfoDTO {
    private int sequence;
    private int duration;
    private int distance;

    // 교통수단일 경우만 채워짐 (service)
    private TransitRouteDetailInfoDTO transitDetail;

}