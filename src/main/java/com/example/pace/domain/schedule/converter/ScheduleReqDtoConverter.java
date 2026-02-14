package com.example.pace.domain.schedule.converter;

import com.example.pace.domain.schedule.dto.request.ScheduleReqDto;
import com.example.pace.domain.schedule.entity.Route;
import com.example.pace.domain.schedule.entity.RepeatRule;
import com.example.pace.domain.schedule.entity.RouteDetail;
import com.example.pace.domain.schedule.entity.Schedule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;


public class ScheduleReqDtoConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    // 요청 dto -> Schedule 엔티티 객체
    public static Schedule toSchedule(ScheduleReqDto source) {
        return Schedule.builder()
                .title(source.getTitle())
                .isAllDay(source.getIsAllDay())
                .startDate(source.getStartDate())
                .endDate(source.getEndDate())
                .startTime(source.getStartTime())
                .endTime(source.getEndTime())
                .memo(source.getMemo())
                .isPathIncluded(source.getIsPathIncluded())
                .build();
    }

    // repeatDto -> RepeatRule 엔티티 객체
    public static RepeatRule toRepeatRule(ScheduleReqDto.RepeatDto repeatDto) {
        if (repeatDto == null) return null;

        return RepeatRule.builder()
                .repeatType(repeatDto.getRepeatType())
                .repeatInterval(repeatDto.getRepeatInterval())
                .daysOfWeek(repeatDto.getDaysOfWeek())
                .endType(repeatDto.getEndType())
                .endCount(repeatDto.getEndCount())
                .endDate(repeatDto.getRepeatEndDate())
                .build();
    }

    public static Route toRoute(ScheduleReqDto.RouteReqDto dto) {
        if (dto == null) return null;
        return Route.builder()
                .originName(dto.getOriginName())
                .originLat(dto.getOriginLat())
                .originLng(dto.getOriginLng())
                .destName(dto.getDestName())
                .destLat(dto.getDestLat())
                .destLng(dto.getDestLng())
                .totalTime(dto.getTotalTime())
                .totalDistance(dto.getTotalDistance())
                .arrivalTime(dto.getArrivalTime())
                .departureTime(dto.getDepartureTime())
                .isSaved(false)
                .build();
    }

    public static RouteDetail toRouteDetail(ScheduleReqDto.RouteDetailReqDto dto) {
        if (dto == null) return null;

        try {
            // DTO 객체 전체를 JSON 문자열로 변환
            String jsonData = objectMapper.writeValueAsString(dto);

            return RouteDetail.builder()
                    .sequence(dto.getSequence()) // 순서는 별도 컬럼으로 저장
                    .data(jsonData)              // 나머지는 JSON으로 저장
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("RouteDetail JSON 변환 중 오류 발생", e);
        }
    }

    private static BigDecimal toBigDecimal(Double value) {
        return value != null ? BigDecimal.valueOf(value) : null;
    }

}
