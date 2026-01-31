package com.example.pace.domain.schedule.infrastructure;

import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
public class GoogleDirectionApiConfig {
    // 환경변수 설정하세요!
    @Value("${GOOGLE_MAPS_API_KEY}")
    private String apiKey;

    @Bean
    public WebClient googleDirectionsWebClient() {
        return WebClient.builder()
                .baseUrl("https://maps.googleapis.com")
                .defaultHeader("Content-Type", "application/json")
                // 요청을 보낼 때마다 자동으로 API 키를 붙여줌.
                .filter(addApiKeyFilter())
                .build();
    }

    // 요청에 API Key를 붙여줌
    private ExchangeFilterFunction addApiKeyFilter() {
        return (request, next) -> {
            // URL을 Builder에 넣음
            URI newUri = UriComponentsBuilder.fromUri(request.url())
                    .queryParam("key", apiKey) // 주소 뒤에 "?key=내_키_값"
                    .build()
                    .toUri();

            // 수정된 주소로 new Request
            ClientRequest newRequest = ClientRequest.from(request)
                    .url(newUri)
                    .build();

            // new request -> 구글서버
            return next.exchange(newRequest);
        };
    }

}
