package com.example.pace.domain.member.converter;

import com.example.pace.domain.member.dto.response.SettingResponseDTO;
import com.example.pace.domain.member.entity.ReminderTime;
import com.example.pace.domain.member.entity.Setting;
import com.example.pace.domain.member.enums.AlarmType;
import java.util.List;

public class SettingConverter {

    private SettingConverter() {}

    public static ReminderTime toEntity(
            Setting setting,
            AlarmType alarmType,
            int minutes
    ) {
        return ReminderTime.builder()
                .setting(setting)
                .alarmType(alarmType)
                .minutes(minutes)
                .build();
    }

    public static SettingResponseDTO toResponse(Setting setting) {

        List<Integer> scheduleTimes = setting.getScheduleReminderTimes();
        List<Integer> departureTimes = setting.getDepartureReminderTimes();

        return SettingResponseDTO.builder()
                .isNotiEnabled(setting.isNotiEnabled())
                .isLocEnabled(setting.isLocEnabled())
                .earlyArrivalTime(setting.getEarlyArrivalTime())
                .isReminderActive(setting.isReminderActive())
                .calendarType(setting.getCalendarType())
                .scheduleReminderTimes(setting.getScheduleReminderTimes())
                .departureReminderTimes(setting.getDepartureReminderTimes())
                .build();
    }
}
