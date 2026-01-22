package com.example.pace.domain.schedule.converter;

import com.example.pace.domain.schedule.dto.request.ScheduleReqDto;
import com.example.pace.domain.schedule.entity.Reminder;

public class ReminderReqDtoConverter {
    // 요청dto -> 알림 엔티티 객체
    public static Reminder toReminder(ScheduleReqDto.ReminderDto reminderDto) {
        return Reminder.builder()
                .reminderType(reminderDto.getReminderType())
                .minutesBefore(reminderDto.getMinutesBefore())
                .reminderEnabled(true) // 기본값 켜짐
                .build();
    }
}
