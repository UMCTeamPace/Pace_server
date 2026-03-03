package com.example.pace.domain.schedule.converter;

import com.example.pace.domain.schedule.dto.request.ScheduleReqDto;
import com.example.pace.domain.schedule.dto.request.ScheduleRouteUpdateReqDto;
import com.example.pace.domain.schedule.dto.response.ScheduleResDto;
import com.example.pace.domain.schedule.dto.response.ScheduleRouteDeleteResDto;
import com.example.pace.domain.schedule.entity.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Comparator.comparing;

@Component
@RequiredArgsConstructor
public class ScheduleConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    // repeatDto -> RepeatRule 엔티티 객체
    public RepeatRule toRepeatRule(ScheduleReqDto.RepeatDto repeatDto) {
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

    public Route toRoute(ScheduleReqDto.RouteReqDto dto) {
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

    public RouteDetail toRouteDetail(ScheduleReqDto.RouteDetailReqDto dto) {
        if (dto == null) return null;
        try {
            String jsonData = objectMapper.writeValueAsString(dto);
            return RouteDetail.builder()
                    .sequence(dto.getSequence())
                    .data(jsonData)
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("RouteDetail(생성) JSON 변환 오류", e);
        }
    }

    public Route toRoute(ScheduleRouteUpdateReqDto req) {
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

    public RouteDetail toRouteDetail(ScheduleRouteUpdateReqDto.RouteDetailUpdateReqDto dto) {
        try {
            // DTO 객체 전체를 JSON 문자열로 변환
            String jsonData = objectMapper.writeValueAsString(dto);
            return RouteDetail.builder()
                    .sequence(dto.getSequence()) // 순서는 별도 컬럼으로 저장
                    .data(jsonData)              // 나머지는 JSON으로 저장
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("RouteDetail(수정) JSON 변환 오류", e);
        }
    }

    // Entity -> Response DTO
    public ScheduleResDto toScheduleResDto(Schedule schedule) {
        return ScheduleResDto.builder()
                .scheduleId(schedule.getId())
                .scheduleInfo(toInfoDto(schedule))
                .place(toPlaceDto(schedule.getPlace()))
                .route(toRouteDto(schedule.getRoute()))
                .reminders(toReminderDtos(schedule.getReminderList()))
                .build();
    }

    public ScheduleRouteDeleteResDto toScheduleRouteDeleteResDto(Schedule schedule) {
        return ScheduleRouteDeleteResDto.of(schedule.getId(), schedule.getUpdatedAt());
    }

    // 기본 정보
    private ScheduleResDto.ScheduleInfoDto toInfoDto(Schedule schedule) {
        return ScheduleResDto.ScheduleInfoDto.builder()
                .title(schedule.getTitle())
                .isAllDay(schedule.getIsAllDay())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .calendarId(schedule.getCalendarId())
                .color(schedule.getColor())
                .memo(schedule.getMemo())
                .build();
    }

    // 장소 정보
    private ScheduleResDto.PlaceDto toPlaceDto(Place place) {
        if (place == null) return null;
        return ScheduleResDto.PlaceDto.builder()
                .targetName(place.getTargetName())
                .targetLat(place.getTargetLat())
                .targetLng(place.getTargetLng())
                .build();
    }

    // 경로 정보
    private ScheduleResDto.RouteResDto toRouteDto(Route route) {
        if (route == null) return null;
        return ScheduleResDto.RouteResDto.builder()
                .originName(route.getOriginName())
                .originLat(route.getOriginLat())
                .originLng(route.getOriginLng())
                .destName(route.getDestName())
                .destLat(route.getDestLat())
                .destLng(route.getDestLng())
                .totalTime(route.getTotalTime())
                .totalDistance(route.getTotalDistance())
                .arrivalTime(route.getArrivalTime())
                .departureTime(route.getDepartureTime())
                .routeDetails(toDetailDtos(route.getRouteDetails()))
                .build();
    }

    // 상세 경로
    private List<ScheduleResDto.RouteDetailResDto> toDetailDtos(List<RouteDetail> details) {
        if (details == null) return List.of();
        return details.stream()
                .sorted(comparing(RouteDetail::getSequence))
                .map(this::toRouteDetailDto)
                .toList();
    }

    // 개별 상세 경로
    private ScheduleResDto.RouteDetailResDto toRouteDetailDto(RouteDetail detail) {
        if (detail == null) return null;
        try {
            return objectMapper.readValue(detail.getData(), ScheduleResDto.RouteDetailResDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("RouteDetail 데이터 파싱 오류", e);
        }
    }

    // 알림 정보
    private List<ScheduleResDto.ReminderDto> toReminderDtos(List<Reminder> reminders) {
        if (reminders == null) return List.of();
        return reminders.stream()
                .map(this::toReminderDto)
                .toList();
    }

    private ScheduleResDto.ReminderDto toReminderDto(Reminder reminder) {
        return ScheduleResDto.ReminderDto.builder()
                .reminderType(reminder.getReminderType())
                .minutesBefore(reminder.getMinutesBefore())
                .reminderEnabled(reminder.getReminderEnabled())
                .build();
    }

    private BigDecimal toBigDecimal(Double value) {
        return value != null ? BigDecimal.valueOf(value) : null;
    }
}