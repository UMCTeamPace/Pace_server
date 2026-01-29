package com.example.pace.domain.schedule.converter;

import com.example.pace.domain.schedule.dto.response.ScheduleResDto.ReminderDto;
import com.example.pace.domain.schedule.dto.response.ScheduleResDto;
import com.example.pace.domain.schedule.dto.response.ScheduleResDto.PlaceDto;
import com.example.pace.domain.schedule.dto.response.ScheduleResDto.RouteDto;
import com.example.pace.domain.schedule.dto.response.ScheduleResDto.ScheduleInfoDto;
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
                                .routeDetails(null)
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
