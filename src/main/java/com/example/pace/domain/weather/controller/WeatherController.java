package com.example.pace.domain.weather.controller;

import com.example.pace.domain.weather.controller.docs.WeatherControllerDocs;
import com.example.pace.domain.weather.dto.request.WeatherReqDTO;
import com.example.pace.domain.weather.dto.response.WeatherResDTO;
import com.example.pace.domain.weather.exception.code.WeatherSuccessCode;
import com.example.pace.domain.weather.service.query.WeatherQueryService;
import com.example.pace.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/weather")
public class WeatherController implements WeatherControllerDocs {
    private final WeatherQueryService weatherQueryService;

    /**
     * 도시명과 국가 코드로 현재 날씨를 조회한다.
     */
    @Override
    @GetMapping("/current")
    public ApiResponse<WeatherResDTO.CurrentWeatherDTO> getCurrentWeather(
            @Valid @ModelAttribute WeatherReqDTO.CurrentWeatherDTO request
    ) {
        return ApiResponse.onSuccess(
                WeatherSuccessCode.WEATHER_CURRENT_OK,
                weatherQueryService.getCurrentWeather(request)
        );
    }
}
