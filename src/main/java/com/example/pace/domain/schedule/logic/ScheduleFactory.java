package com.example.pace.domain.schedule.logic;


import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.schedule.converter.PlaceReqDtoConverter;
import com.example.pace.domain.schedule.converter.ReminderReqDtoConverter;
import com.example.pace.domain.schedule.converter.ScheduleReqDtoConverter;
import com.example.pace.domain.schedule.converter.ScheduleRouteUpdateReqDtoConverter;
import com.example.pace.domain.schedule.dto.request.ScheduleReqDto;
import com.example.pace.domain.schedule.dto.request.ScheduleUpdateReqDto;
import com.example.pace.domain.schedule.entity.RepeatRule;
import com.example.pace.domain.schedule.entity.Route;
import com.example.pace.domain.schedule.entity.Schedule;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class ScheduleFactory {

    public Schedule create(
            Member member,
            LocalDate date,
            LocalDate endDate,
            ScheduleReqDto request,
            RepeatRule repeatRule,
            String groupId
    ) {
        Schedule schedule = Schedule.builder()
                .title(request.getTitle())
                .isAllDay(request.getIsAllDay())
                .memo(request.getMemo())
                .startDate(date)
                .endDate(endDate)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .isRepeat(request.getIsRepeat())
                .repeatGroupId(groupId)
                .repeatRule(repeatRule)
                .member(member)
                .isPathIncluded(Boolean.TRUE.equals(request.getIsPathIncluded()))
                .build();

        if (!Boolean.TRUE.equals(request.getIsPathIncluded()) && request.getPlace() != null) {
            schedule.addPlace(PlaceReqDtoConverter.toPlace(request.getPlace()));
        }

        //경로 저장
        if (Boolean.TRUE.equals(request.getIsPathIncluded()) && request.getRoute() != null) {
            Route route = ScheduleReqDtoConverter.toRoute(request.getRoute());
            schedule.addRoute(route);

            if (request.getRoute().getRouteDetails() != null) {
                request.getRoute().getRouteDetails().stream()
                        .filter(java.util.Objects::nonNull)
                        .map(ScheduleReqDtoConverter::toRouteDetail)
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
    public Schedule createFromUpdate(
            Schedule existingSchedule,
            LocalDate date,
            LocalDate endDate,
            ScheduleUpdateReqDto request,
            RepeatRule repeatRule,
            String repeatGroupId
    ) {
        Schedule schedule = Schedule.builder()
                .member(existingSchedule.getMember())
                .title(request.getTitle() != null ? request.getTitle() : existingSchedule.getTitle())
                .memo(request.getMemo() != null ? request.getMemo() : existingSchedule.getMemo())
                .isAllDay(request.getIsAllDay() != null ? request.getIsAllDay() : existingSchedule.getIsAllDay())
                .startTime(request.getStartTime() != null ? request.getStartTime() : existingSchedule.getStartTime())
                .endTime(request.getEndTime() != null ? request.getEndTime() : existingSchedule.getEndTime())
                .startDate(date)
                .endDate(endDate)
                .repeatGroupId(repeatGroupId)
                .isRepeat(repeatGroupId != null)
                .repeatRule(repeatRule)
                .isPathIncluded(request.getIsPathIncluded() != null ? request.getIsPathIncluded() : existingSchedule.getIsPathIncluded())
                .build();

        if (request.getPlace() != null) {
            schedule.addPlace(PlaceReqDtoConverter.toPlace(request.getPlace()));
        } else if (existingSchedule.getPlace() != null) {
            schedule.addPlace(PlaceReqDtoConverter.toPlaceFromExisting(existingSchedule.getPlace()));
        }

        if (request.getReminders() != null) {
            request.getReminders().forEach(dto ->
                    schedule.addReminder(ReminderReqDtoConverter.toReminder(dto))
            );
        } else if (existingSchedule.getReminderList() != null) {
            existingSchedule.getReminderList().forEach(existingReminder ->
                    schedule.addReminder(ReminderReqDtoConverter.toReminderFromExisting(existingReminder))
            );
        }

        return schedule;
    }
}
