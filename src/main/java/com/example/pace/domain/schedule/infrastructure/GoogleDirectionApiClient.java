package com.example.pace.domain.schedule.infrastructure;


import static com.example.pace.domain.schedule.converter.RouteResDTOConverter.localDateTimeToEpoch;

import com.example.pace.domain.schedule.dto.request.DirectionRequestDTO;
import com.example.pace.domain.schedule.infrastructure.dto.GoogleDirectionApiResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleDirectionApiClient {

    private final WebClient googleDirectionsWebClient;
    private static final String MODE = "transit";


    public GoogleDirectionApiResponse getDirections(DirectionRequestDTO request) {

        String origin = request.getOrigin();
        String destination = request.getDestination();
        Long departureTime = request.getDepartureTime();
        Long arrivalTime = request.getArrivalTime();
        String transitMode = request.getTransitMode();
        String routingPreference = request.getRoutingPreference();

        return googleDirectionsWebClient.get()
                .uri(uriBuilder -> {
                    uriBuilder
                            .path("/maps/api/directions/json")
                            .queryParam("origin", origin)
                            .queryParam("language", "ko") // 설명 한국어로 설정
                            .queryParam("destination", destination)
                            .queryParam("alternatives", true)
                            .queryParam("mode", MODE);

                    //도착시간or출발시간 분기처리
                    if (arrivalTime != null) {
                        uriBuilder.queryParam("arrival_time", arrivalTime);
                    } else if (departureTime != null) {
                        // 도착 시간이 없을 때만 출발 시간을 설정
                        uriBuilder.queryParam("departure_time", departureTime);
                    } else {
                        // 둘 다 없으면 현재 시간으로 설정
                        uriBuilder.queryParam("departure_time", "now");
                    }

                    if (transitMode != null) {
                        uriBuilder.queryParam("transit_mode", transitMode);
                    }

                    if (routingPreference != null) {
                        uriBuilder.queryParam("transit_routing_preference", routingPreference);
                    }

                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(GoogleDirectionApiResponse.class)
                .block();
    }
}
