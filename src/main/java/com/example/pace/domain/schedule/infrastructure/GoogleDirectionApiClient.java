package com.example.pace.domain.schedule.infrastructure;


import static org.springframework.web.reactive.function.server.RequestPredicates.queryParam;

import com.example.pace.domain.schedule.dto.request.DirectionRequestDTO;
import com.example.pace.domain.schedule.infrastructure.dto.GoogleDirectionApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class GoogleDirectionApiClient {

    private final WebClient googleDirectionsWebClient;
    private static final String TRANSIT_MODE = "transit";

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
                            .queryParam("destination", destination)
                            .queryParam("mode", TRANSIT_MODE);//final 변수 사용해서 대중교통 모드 지정

                    //도착시간or출발시간 분기처리
                    if (arrivalTime != null) {
                        uriBuilder.queryParam("arrival_time", arrivalTime);
                    } else if (departureTime != null) {
                        // 도착 시간이 없을 때만 출발 시간을 설정
                        uriBuilder.queryParam("departure_time", departureTime);
                    } else {
                        //'departure_time=now' 기본값(생략 가능)
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
