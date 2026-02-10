package com.example.pace.domain.schedule.converter;

import com.example.pace.domain.schedule.dto.response.ScheduleResDto;
import com.example.pace.domain.schedule.entity.Place;
import com.example.pace.domain.schedule.entity.Reminder;
import com.example.pace.domain.schedule.entity.Route;
import com.example.pace.domain.schedule.entity.RouteDetail;
import com.example.pace.domain.schedule.entity.Schedule;

import java.util.List;

import static java.util.Comparator.comparing;

public class ScheduleResDtoConverter {

    public static ScheduleResDto toScheduleResDto(Schedule schedule) {
        return ScheduleResDto.builder()
                .scheduleId(schedule.getId())
                .scheduleInfo(toInfoDto(schedule))
                .place(toPlaceDto(schedule.getPlace()))
                .route(toRouteDto(schedule.getRoute()))
                .reminders(toReminderDtos(schedule.getReminderList()))
                .build();
    }

    // 기본 정보
    private static ScheduleResDto.ScheduleInfoDto toInfoDto(Schedule schedule) {
        return ScheduleResDto.ScheduleInfoDto.builder()
                .title(schedule.getTitle())
                .isAllDay(schedule.getIsAllDay())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .memo(schedule.getMemo())
                .build();
    }

    // 장소 정보
    private static ScheduleResDto.PlaceDto toPlaceDto(Place place) {
        if (place == null) return null;
        return ScheduleResDto.PlaceDto.builder()
                .targetName(place.getTargetName())
                .targetLat(place.getTargetLat())
                .targetLng(place.getTargetLng())
                .build();
    }

    // 경로 정보
    private static ScheduleResDto.RouteDto toRouteDto(Route route) {
        if (route == null) return null;
        return ScheduleResDto.RouteDto.builder()
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
    private static List<ScheduleResDto.RouteDetailDto> toDetailDtos(List<RouteDetail> details) {
        if (details == null) return List.of();
        return details.stream()
                .sorted(comparing(RouteDetail::getSequence))
                .map(ScheduleResDtoConverter::toRouteDetailDto)
                .toList();
    }

    // 개별 상세 경로
    private static ScheduleResDto.RouteDetailDto toRouteDetailDto(RouteDetail detail) {
        return ScheduleResDto.RouteDetailDto.builder()
                .sequence(detail.getSequence())
                .description(detail.getDescription())
                .transitType(detail.getTransitType() != null ? detail.getTransitType().name() : null)
                .duration(detail.getDuration())
                .distance(detail.getDistance())
                .lineName(detail.getLineName())
                .lineColor(detail.getLineColor())
                .departureStop(detail.getDepartureStop())
                .arrivalStop(detail.getArrivalStop())
                .shortName(detail.getShortName())
                .startLat(detail.getStartLat())
                .startLng(detail.getStartLng())
                .endLat(detail.getEndLat())
                .endLng(detail.getEndLng())
                .build();
    }

    // 알림 정보
    private static List<ScheduleResDto.ReminderDto> toReminderDtos(List<Reminder> reminders) {
        if (reminders == null) return List.of();
        return reminders.stream()
                .map(ScheduleResDtoConverter::toReminderDto)
                .toList();
    }

    private static ScheduleResDto.ReminderDto toReminderDto(Reminder reminder) {
        return ScheduleResDto.ReminderDto.builder()
                .reminderType(reminder.getReminderType())
                .minutesBefore(reminder.getMinutesBefore())
                .reminderEnabled(reminder.getReminderEnabled())
                .build();
    }
}