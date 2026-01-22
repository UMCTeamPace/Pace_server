package com.example.pace.domain.schedule.dto.response.info;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RouteDetailInfoResDTO {


    public static class RouteDetailInfoRes {
        private Integer sequence;
        private Integer duration;
        private Integer distance;

        // 교통수단일 경우만 채워짐 (service)
        private TransitRouteDetailInfoResDTO transitDetail;

    }
}