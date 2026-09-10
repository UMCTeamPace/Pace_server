package com.example.pace.domain.weather.service.query;

import com.example.pace.domain.weather.converter.WeatherConverter;
import com.example.pace.domain.weather.dto.request.WeatherReqDTO;
import com.example.pace.domain.weather.dto.response.WeatherResDTO;
import com.example.pace.domain.weather.exception.WeatherException;
import com.example.pace.domain.weather.exception.code.WeatherErrorCode;
import com.example.pace.domain.weather.infrastructure.OpenWeatherApiClient;
import com.example.pace.domain.weather.infrastructure.dto.OpenWeatherCurrentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
public class WeatherQueryService {
    private final OpenWeatherApiClient openWeatherApiClient;

    /**
     * 요청한 도시와 국가 코드에 대한 현재 날씨를 조회한다.
     *
     * @throws WeatherException 외부 API 오류 또는 필수 응답 필드가 누락된 경우
     */
    public WeatherResDTO.CurrentWeatherDTO getCurrentWeather(
            WeatherReqDTO.CurrentWeatherDTO request
    ) {
        try {
            OpenWeatherCurrentResponse response = openWeatherApiClient.getCurrentWeather(
                    request.getCity(),
                    request.getCountryCode()
            );
            validateResponse(response);
            return WeatherConverter.toCurrentWeather(response);
        } catch (WebClientResponseException e) {
            throw toWeatherException(e);
        } catch (WebClientRequestException e) {
            throw new WeatherException(WeatherErrorCode.WEATHER_API_UNAVAILABLE);
        }
    }

    private void validateResponse(OpenWeatherCurrentResponse response) {
        if (response == null
                || response.getName() == null
                || response.getWeather() == null
                || response.getWeather().isEmpty()
                || response.getWeather().getFirst() == null
                || response.getWeather().getFirst().getMain() == null
                || response.getWeather().getFirst().getDescription() == null
                || response.getMain() == null
                || response.getMain().getTemp() == null) {
            throw new WeatherException(WeatherErrorCode.WEATHER_RESPONSE_INVALID);
        }
    }

    private WeatherException toWeatherException(WebClientResponseException exception) {
        if (exception.getStatusCode().value() == 404) {
            return new WeatherException(WeatherErrorCode.WEATHER_LOCATION_NOT_FOUND);
        }

        if (exception.getStatusCode().is4xxClientError()) {
            return new WeatherException(WeatherErrorCode.WEATHER_API_BAD_REQUEST);
        }

        return new WeatherException(WeatherErrorCode.WEATHER_API_UNAVAILABLE);
    }
}
