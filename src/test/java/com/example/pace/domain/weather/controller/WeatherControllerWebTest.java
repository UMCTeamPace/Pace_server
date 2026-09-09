package com.example.pace.domain.weather.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.pace.domain.weather.dto.response.WeatherResDTO;
import com.example.pace.domain.weather.enums.WeatherConditionCode;
import com.example.pace.domain.weather.service.query.WeatherQueryService;
import com.example.pace.global.auth.CustomUserDetailsService;
import com.example.pace.global.apiPayload.handler.GeneralExceptionAdvice;
import com.example.pace.global.util.JwtUtil;
import com.example.pace.global.util.RedisUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(WeatherController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {WeatherController.class, GeneralExceptionAdvice.class})
class WeatherControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WeatherQueryService weatherQueryService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private RedisUtil redisUtil;

    @Test
    @DisplayName("도시와 국가 코드로 현재 날씨를 조회하면 공통 성공 응답을 반환한다")
    void getCurrentWeather_validRequest_returnsSuccessResponse() throws Exception {
        // given
        given(weatherQueryService.getCurrentWeather(any()))
                .willReturn(WeatherResDTO.CurrentWeatherDTO.builder()
                        .locationName("Seoul")
                        .conditionCode(WeatherConditionCode.CLEAR)
                        .description("맑음")
                        .temperatureCelsius(23.4)
                        .build());

        // when
        var result = mockMvc.perform(get("/api/v1/weather/current")
                .param("city", "Seoul")
                .param("countryCode", "KR"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value("WEATHER200_1"))
                .andExpect(jsonPath("$.result.locationName").value("Seoul"))
                .andExpect(jsonPath("$.result.conditionCode").value("CLEAR"));
    }

    @Test
    @DisplayName("도시명이 없으면 validation 오류를 반환하고 Service를 호출하지 않는다")
    void getCurrentWeather_missingCity_returnsBadRequest() throws Exception {
        // given

        // when
        var result = mockMvc.perform(get("/api/v1/weather/current")
                .param("countryCode", "KR"));

        // then
        result.andExpect(status().isBadRequest());
        verifyNoInteractions(weatherQueryService);
    }
}
