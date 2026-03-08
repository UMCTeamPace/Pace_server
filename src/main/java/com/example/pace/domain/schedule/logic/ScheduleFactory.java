package com.example.pace.domain.schedule.logic;


import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.schedule.converter.PlaceReqDtoConverter;
import com.example.pace.domain.schedule.converter.ReminderReqDtoConverter;
import com.example.pace.domain.schedule.converter.ScheduleConverter;
import com.example.pace.domain.schedule.dto.request.ScheduleReqDto;
import com.example.pace.domain.schedule.entity.Route;
import com.example.pace.domain.schedule.entity.Schedule;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class ScheduleFactory {

    public Schedule create(
            Member member,
            LocalDate date,
            LocalDate endDate,
            ScheduleReqDto request
    ) {
        Schedule schedule = Schedule.builder()
                .title(request.getTitle())
                .memo(request.getMemo())
                .startDate(date)
                .endDate(endDate)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .calendarId(request.getCalendarId())
                .color(request.getColor())
                .member(member)
                .isPathIncluded(Boolean.TRUE.equals(request.getIsPathIncluded()))
                .build();

        if (!Boolean.TRUE.equals(request.getIsPathIncluded()) && request.getPlace() != null) {
            schedule.addPlace(PlaceReqDtoConverter.toPlace(request.getPlace()));
        }

        //경로 저장
        if (Boolean.TRUE.equals(request.getIsPathIncluded()) && request.getRoute() != null) {
            Route route = ScheduleConverter.toRoute(request.getRoute());
            schedule.addRoute(route);

            if (request.getRoute().getRouteDetails() != null) {
                request.getRoute().getRouteDetails().stream()
                        .filter(java.util.Objects::nonNull)
                        .map(ScheduleConverter::toRouteDetail)
                        .forEach(route::addRouteDetail);
            }
        }

        if (request.getReminders() != null) {
            request.getReminders().forEach(dto ->
                    schedule.addReminder(ReminderReqDtoConverter.toReminder(dto))
            );
        }

        return schedule;
    }

}
