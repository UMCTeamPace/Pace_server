package com.example.pace.domain.weather.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class WeatherReqDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CurrentWeatherDTO {
        @NotBlank(message = "도시명은 필수입니다.")
        private String city;

        @NotBlank(message = "국가 코드는 필수입니다.")
        private String countryCode;
    }
}
