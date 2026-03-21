package com.example.pace.domain.transit.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class BusInfoReqDTO {
    @NoArgsConstructor
    @Getter
    @Setter
    public static class BusStartStationDTO {
        @NotBlank(message = "노선 번호는 필수입니다.")
        String lineName;
        @NotBlank(message = "출발 정류장 이름은 필수입니다.")
        String startStation;
        @NotBlank(message = "도착 정류장 이름은 필수입니다.")
        String endStation;
    }
}
