package com.example.pace.domain.schedule.dto.response.info;

import com.example.pace.domain.schedule.enums.TransitType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class TransitRouteDetailInfoResDTO {
    @Builder
    @Getter
    @AllArgsConstructor
    public static class RouteDetailInfoDTO {
        private TransitType transitType;
        private String lineName;
        private String lineColor;
        private Integer stopCount;
        private String departureStop;
        private String arrivalStop;
        private String description; //이고 안내문구 무슨 안내문구인가요? (예: 00역에서 내려서 2분동안 걸으세요?)
    }
}