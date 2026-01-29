package com.example.pace.domain.schedule.logic;


import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.schedule.converter.PlaceReqDtoConverter;
import com.example.pace.domain.schedule.converter.ReminderReqDtoConverter;
import com.example.pace.domain.schedule.dto.request.ScheduleReqDto;
import com.example.pace.domain.schedule.entity.RepeatRule;
import com.example.pace.domain.schedule.entity.Schedule;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class ScheduleFactory {

    public Schedule create(Member member, LocalDate date, ScheduleReqDto request, RepeatRule repeatRule, String groupId) {
        Schedule schedule = Schedule.builder()
                .title(request.getTitle())
                .isAllDay(request.getIsAllDay())
                .memo(request.getMemo())
                .startDate(date)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .isRepeat(request.getIsRepeat())
                .repeatGroupId(groupId)
                .repeatRule(repeatRule)
                .member(member)
                .build();

        if (!Boolean.TRUE.equals(request.getIsPathIncluded()) && request.getPlace() != null) {
            schedule.addPlace(PlaceReqDtoConverter.toPlace(request.getPlace()));
        }

        /* * 경로 저장
         * if (Boolean.TRUE.equals(request.getIsPathIncluded()) && request.getRoute() != null) {
         * }
         */

        if (request.getReminders() != null) {
            request.getReminders().forEach(dto ->
                    schedule.addReminder(ReminderReqDtoConverter.toReminder(dto))
            );
        }

        return schedule;
    }
}
