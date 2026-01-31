package com.example.pace.domain.schedule.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DirectionRequestDTO {

    private String origin;
    private String destination;
    private Long departureTime;
    private Long arrivalTime;
    private String transitMode;
    private String routingPreference;
}
