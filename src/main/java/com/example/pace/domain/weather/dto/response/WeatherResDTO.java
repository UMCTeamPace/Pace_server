package com.example.pace.domain.weather.dto.response;

import com.example.pace.domain.weather.enums.WeatherConditionCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class WeatherResDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CurrentWeatherDTO {
        private String locationName;
        private WeatherConditionCode conditionCode;
        private String description;
        private Double temperatureCelsius;
    }
}
