package com.example.pace.domain.weather.infrastructure;

import com.example.pace.domain.weather.infrastructure.dto.OpenWeatherCurrentResponse;
import com.example.pace.global.config.OpenWeatherProperties;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OpenWeatherApiClient {
    private final WebClient webClient;
    private final OpenWeatherProperties openWeatherProperties;

    /**
     * 도시와 국가 코드로 OpenWeather 현재 날씨 원본 응답을 조회한다.
     */
    public OpenWeatherCurrentResponse getCurrentWeather(String city, String countryCode) {
        URI requestUri = UriComponentsBuilder
                .fromUriString(openWeatherProperties.getCurrentWeatherUrl())
                .queryParam("q", city + "," + countryCode)
                .queryParam("units", "metric")
                .queryParam("lang", "kr")
                .queryParam("appid", openWeatherProperties.getApiKey())
                .build()
                .encode()
                .toUri();

        return webClient.get()
                .uri(requestUri)
                .retrieve()
                .bodyToMono(OpenWeatherCurrentResponse.class)
                .block();
    }
}
