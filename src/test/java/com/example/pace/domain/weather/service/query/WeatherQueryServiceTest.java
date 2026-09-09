package com.example.pace.domain.weather.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import com.example.pace.domain.weather.dto.request.WeatherReqDTO;
import com.example.pace.domain.weather.dto.response.WeatherResDTO;
import com.example.pace.domain.weather.exception.WeatherException;
import com.example.pace.domain.weather.exception.code.WeatherErrorCode;
import com.example.pace.domain.weather.infrastructure.OpenWeatherApiClient;
import com.example.pace.domain.weather.infrastructure.dto.OpenWeatherCurrentResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@ExtendWith(MockitoExtension.class)
class WeatherQueryServiceTest {

    @InjectMocks
    private WeatherQueryService weatherQueryService;

    @Mock
    private OpenWeatherApiClient openWeatherApiClient;

    @Test
    @DisplayName("정상적인 외부 응답을 앱 날씨 응답으로 반환한다")
    void getCurrentWeather_validResponse_returnsCurrentWeather() throws Exception {
        // given
        WeatherReqDTO.CurrentWeatherDTO request = request();
        given(openWeatherApiClient.getCurrentWeather("Seoul", "KR"))
                .willReturn(response("Seoul", "Clear", "맑음", 23.4));

        // when
        WeatherResDTO.CurrentWeatherDTO result = weatherQueryService.getCurrentWeather(request);

        // then
        assertThat(result.getLocationName()).isEqualTo("Seoul");
        assertThat(result.getConditionCode().name()).isEqualTo("CLEAR");
        assertThat(result.getDescription()).isEqualTo("맑음");
        assertThat(result.getTemperatureCelsius()).isEqualTo(23.4);
    }

    @Test
    @DisplayName("지역을 찾지 못하면 위치 조회 예외를 반환한다")
    void getCurrentWeather_locationNotFound_throwsLocationNotFound() {
        // given
        WeatherReqDTO.CurrentWeatherDTO request = request();
        given(openWeatherApiClient.getCurrentWeather("Seoul", "KR"))
                .willThrow(WebClientResponseException.create(
                        404,
                        "Not Found",
                        HttpHeaders.EMPTY,
                        new byte[0],
                        StandardCharsets.UTF_8
                ));

        // when
        WeatherException exception = assertThrows(
                WeatherException.class,
                () -> weatherQueryService.getCurrentWeather(request)
        );

        // then
        assertThat(exception.getCode()).isEqualTo(WeatherErrorCode.WEATHER_LOCATION_NOT_FOUND);
    }

    @Test
    @DisplayName("필수 응답 필드가 없으면 응답 오류를 반환한다")
    void getCurrentWeather_missingRequiredField_throwsInvalidResponse() throws Exception {
        // given
        WeatherReqDTO.CurrentWeatherDTO request = request();
        given(openWeatherApiClient.getCurrentWeather("Seoul", "KR"))
                .willReturn(response("Seoul", null, "맑음", 23.4));

        // when
        WeatherException exception = assertThrows(
                WeatherException.class,
                () -> weatherQueryService.getCurrentWeather(request)
        );

        // then
        assertThat(exception.getCode()).isEqualTo(WeatherErrorCode.WEATHER_RESPONSE_INVALID);
    }

    private WeatherReqDTO.CurrentWeatherDTO request() {
        WeatherReqDTO.CurrentWeatherDTO request = new WeatherReqDTO.CurrentWeatherDTO();
        request.setCity("Seoul");
        request.setCountryCode("KR");
        return request;
    }

    private OpenWeatherCurrentResponse response(
            String name,
            String condition,
            String description,
            double temperature
    ) throws Exception {
        return new ObjectMapper().readValue(
                "{"
                        + "\"name\":\"" + name + "\","
                        + "\"weather\":[{\"main\":"
                        + (condition == null ? "null" : "\"" + condition + "\"")
                        + ",\"description\":\"" + description + "\"}],"
                        + "\"main\":{\"temp\":" + temperature + "}"
                        + "}",
                OpenWeatherCurrentResponse.class
        );
    }
}
