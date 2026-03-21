package com.example.pace.domain.transit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BusInfoResDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BusInfoDTO {
        private String routeId;
        private String nodeId;
        private Integer sequence;
    }
}
