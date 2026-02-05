package com.example.pace.domain.schedule.converter;

import com.example.pace.domain.schedule.dto.response.ScheduleResDto.ReminderDto;
import com.example.pace.domain.schedule.dto.response.ScheduleResDto;
import com.example.pace.domain.schedule.dto.response.ScheduleResDto.PlaceDto;
import com.example.pace.domain.schedule.dto.response.ScheduleResDto.RouteDto;
import com.example.pace.domain.schedule.dto.response.ScheduleResDto.ScheduleInfoDto;
import com.example.pace.domain.schedule.entity.RouteDetail;
import com.example.pace.domain.schedule.entity.Schedule;

public class ScheduleResDtoConverter {
    public static ScheduleResDto toScheduleResDto(Schedule schedule) {
        return ScheduleResDto.builder()
                .scheduleId(schedule.getId())
                .scheduleInfo(ScheduleInfoDto.builder()
                        .title(schedule.getTitle())
                        .isAllDay(schedule.getIsAllDay())
                        .startDate(schedule.getStartDate())
                        .endDate(schedule.getEndDate())
                        .startTime(schedule.getStartTime())
                        .endTime(schedule.getEndTime())
                        .memo(schedule.getMemo())
                        .build())
                .place(schedule.getPlace() != null ?
                        PlaceDto.builder()
                                .targetName(schedule.getPlace().getTargetName())
                                .targetLat(schedule.getPlace().getTargetLat())
                                .targetLng(schedule.getPlace().getTargetLng())
                                .build() : null)
                .route(schedule.getRoute() != null ?
                        RouteDto.builder()
                                .originName(schedule.getRoute().getOriginName())
                                .originLat(schedule.getRoute().getOriginLat())
                                .originLng(schedule.getRoute().getOriginLng())
                                .destName(schedule.getRoute().getDestName())
                                .destLat(schedule.getRoute().getDestLat())
                                .destLng(schedule.getRoute().getDestLng())
                                .totalTime(schedule.getRoute().getTotalTime())
                                .totalDistance(schedule.getRoute().getTotalDistance())
                                .routeDetails(schedule.getRoute().getRouteDetails() != null ?
                                        schedule.getRoute().getRouteDetails().stream()
                                                .sorted(java.util.Comparator.comparing(RouteDetail::getSequence)) // 순환 경로 방지를 위한 정렬
                                                .map(detail -> ScheduleResDto.RouteDetailDto.builder()
                                                        .sequence(detail.getSequence())
                                                        .description(detail.getDescription())
                                                        .transitType(detail.getTransitType() != null ? detail.getTransitType().name() : null)
                                                        .duration(detail.getDuration())
                                                        .distance(detail.getDistance())
                                                        .lineName(detail.getLineName())
                                                        .lineColor(detail.getLineColor())
                                                        .departureStop(detail.getDepartureStop())
                                                        .arrivalStop(detail.getArrivalStop())
                                                        .startLat(detail.getStartLat())
                                                        .startLng(detail.getStartLng())
                                                        .endLat(detail.getEndLat()) 
                                                        .endLng(detail.getEndLng())
                                                        .build())
                                                .toList() : null)
                                .build() : null)
                .reminders(schedule.getReminderList() != null ?
                        schedule.getReminderList().stream()
                                .map(reminder -> ReminderDto.builder()
                                        .reminderType(reminder.getReminderType())
                                        .minutesBefore(reminder.getMinutesBefore())
                                        .reminderEnabled(reminder.getReminderEnabled())
                                        .build())
                                .toList() : null)

                .build();

    }
}
