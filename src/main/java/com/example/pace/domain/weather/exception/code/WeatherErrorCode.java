package com.example.pace.domain.weather.exception.code;

import com.example.pace.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum WeatherErrorCode implements BaseErrorCode {
    WEATHER_LOCATION_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "날씨를 조회할 지역을 찾을 수 없습니다.",
            "WEATHER404_1"
    ),
    WEATHER_API_BAD_REQUEST(
            HttpStatus.BAD_REQUEST,
            "날씨 API 요청이 올바르지 않습니다.",
            "WEATHER400_1"
    ),
    WEATHER_API_UNAVAILABLE(
            HttpStatus.BAD_GATEWAY,
            "날씨 API를 사용할 수 없습니다.",
            "WEATHER502_1"
    ),
    WEATHER_RESPONSE_INVALID(
            HttpStatus.BAD_GATEWAY,
            "날씨 API 응답을 처리할 수 없습니다.",
            "WEATHER502_2"
    );

    private final HttpStatus httpStatus;
    private final String message;
    private final String code;
}
