package com.example.pace.domain.transit.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class SubwayArrivalReqDTO {
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class SubwayArrivalDTO {
        private String startStationName;
        private String endStationName;
        private String lineName;
    }
}
