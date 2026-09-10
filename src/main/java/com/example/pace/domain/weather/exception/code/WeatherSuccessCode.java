package com.example.pace.domain.weather.exception.code;

import com.example.pace.global.apiPayload.code.BaseSuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum WeatherSuccessCode implements BaseSuccessCode {
    WEATHER_CURRENT_OK(
            HttpStatus.OK,
            "현재 날씨를 성공적으로 조회했습니다.",
            "WEATHER200_1"
    );

    private final HttpStatus httpStatus;
    private final String message;
    private final String code;
}
