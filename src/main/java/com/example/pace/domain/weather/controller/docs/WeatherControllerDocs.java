package com.example.pace.domain.weather.controller.docs;

import com.example.pace.domain.weather.dto.request.WeatherReqDTO;
import com.example.pace.domain.weather.dto.response.WeatherResDTO;
import com.example.pace.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.ModelAttribute;

@Tag(name = "Weather", description = "현재 날씨 관련 API")
public interface WeatherControllerDocs {

    @Operation(
            summary = "현재 날씨 조회",
            description = "도시명과 국가 코드를 기준으로 현재 날씨를 조회합니다."
    )
    ApiResponse<WeatherResDTO.CurrentWeatherDTO> getCurrentWeather(
            @ModelAttribute WeatherReqDTO.CurrentWeatherDTO request
    );
}
