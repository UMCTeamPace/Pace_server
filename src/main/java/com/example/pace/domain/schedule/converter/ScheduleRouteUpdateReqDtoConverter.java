package com.example.pace.domain.schedule.converter;

import com.example.pace.domain.schedule.dto.request.ScheduleRouteUpdateReqDto;
import com.example.pace.domain.schedule.entity.Route;
import com.example.pace.domain.schedule.entity.RouteDetail;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ScheduleRouteUpdateReqDtoConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static Route toRoute(ScheduleRouteUpdateReqDto req) {
        return Route.builder()
                .originName(req.getOriginName())
                .originLat(req.getOriginLat())
                .originLng(req.getOriginLng())
                .destName(req.getDestName())
                .destLat(req.getDestLat())
                .destLng(req.getDestLng())
                .totalTime(req.getTotalTime())
                .totalDistance(req.getTotalDistance())
                .arrivalTime(req.getArrivalTime())
                .departureTime(req.getDepartureTime())
                .isSaved(false)
                .build();
    }

    public static RouteDetail toRouteDetail(ScheduleRouteUpdateReqDto.RouteDetailUpdateReqDto dto) {
        try {
            String jsonData = objectMapper.writeValueAsString(dto);

            return RouteDetail.builder()
                    .sequence(dto.getSequence())
                    .data(jsonData) 
                    .build();
        } catch (JsonProcessingException e) {
            // JSON 변환 실패 시 예외 처리
            throw new RuntimeException("RouteDetail 데이터 변환 중 오류가 발생했습니다.", e);
        }
    }
}