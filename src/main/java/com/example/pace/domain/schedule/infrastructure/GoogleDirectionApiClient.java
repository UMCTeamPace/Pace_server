package com.example.pace.domain.schedule.infrastructure;


import com.example.pace.domain.schedule.infrastructure.dto.GoogleDirectionApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class GoogleDirectionApiClient {

    private final WebClient googleDirectionsWebClient;

    public GoogleDirectionApiResponse getDirections(
            String origin,
            String destination,
            Long departureTime,
            Long arrivalTime,
            String transitMode,
            String routingPreference
    ) {
        return googleDirectionsWebClient.get()
                .uri(uriBuilder -> {
                    uriBuilder
                            .path("/maps/api/directions/json")
                            .queryParam("origin", origin)
                            .queryParam("destination", destination)
                            .queryParam("mode", "transit"); // 대중교통 모드 고정

                    if (arrivalTime != null) {
                        uriBuilder.queryParam("arrival_time", arrivalTime);
                    }

                    if (departureTime != null) {
                        uriBuilder.queryParam("departure_time", departureTime);
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
