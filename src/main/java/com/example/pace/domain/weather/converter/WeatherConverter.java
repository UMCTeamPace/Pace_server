package com.example.pace.domain.weather.converter;

import com.example.pace.domain.weather.dto.response.WeatherResDTO;
import com.example.pace.domain.weather.enums.WeatherConditionCode;
import com.example.pace.domain.weather.infrastructure.dto.OpenWeatherCurrentResponse;
import java.util.Locale;

public final class WeatherConverter {
    private WeatherConverter() {
    }

    /**
     * 검증된 OpenWeather 원본 응답을 현재 날씨 앱 응답으로 변환한다.
     */
    public static WeatherResDTO.CurrentWeatherDTO toCurrentWeather(
            OpenWeatherCurrentResponse response
    ) {
        OpenWeatherCurrentResponse.Weather weather = response.getWeather().getFirst();

        return WeatherResDTO.CurrentWeatherDTO.builder()
                .locationName(response.getName())
                .conditionCode(toConditionCode(weather.getMain()))
                .description(weather.getDescription())
                .temperatureCelsius(response.getMain().getTemp())
                .build();
    }

    /**
     * OpenWeather 상태 문자열을 앱의 고정된 날씨 상태 코드로 변환한다.
     */
    public static WeatherConditionCode toConditionCode(String condition) {
        if (condition == null) {
            return WeatherConditionCode.UNKNOWN;
        }

        return switch (condition.toUpperCase(Locale.ROOT)) {
            case "CLEAR" -> WeatherConditionCode.CLEAR;
            case "CLOUDS" -> WeatherConditionCode.CLOUDS;
            case "RAIN" -> WeatherConditionCode.RAIN;
            case "DRIZZLE" -> WeatherConditionCode.DRIZZLE;
            case "THUNDERSTORM" -> WeatherConditionCode.THUNDERSTORM;
            case "SNOW" -> WeatherConditionCode.SNOW;
            case "MIST", "SMOKE", "HAZE", "DUST", "FOG", "SAND", "ASH", "SQUALL", "TORNADO" ->
                    WeatherConditionCode.ATMOSPHERE;
            default -> WeatherConditionCode.UNKNOWN;
        };
    }
}
