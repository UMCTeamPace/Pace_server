package com.example.pace.domain.member.converter;

import com.example.pace.domain.member.dto.response.SettingResponseDTO;
import com.example.pace.domain.member.entity.ReminderTime;
import com.example.pace.domain.member.entity.Setting;
import com.example.pace.domain.member.enums.AlarmType;
import java.util.List;
import java.util.Map;

public class SettingConverter {

    private SettingConverter() {
    }

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

        Map<AlarmType, List<Integer>> timesByType = setting.getReminderTimes().stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        com.example.pace.domain.member.entity.ReminderTime::getAlarmType,
                        java.util.stream.Collectors.mapping(
                                com.example.pace.domain.member.entity.ReminderTime::getMinutes,
                                java.util.stream.Collectors.toList())
                ));

        java.util.List<Integer> scheduleTimes = timesByType.getOrDefault(
                com.example.pace.domain.member.enums.AlarmType.SCHEDULE, java.util.List.of());
        java.util.List<Integer> departureTimes = timesByType.getOrDefault(
                com.example.pace.domain.member.enums.AlarmType.DEPARTURE, java.util.List.of());

        return SettingResponseDTO.builder()
                .isNotiEnabled(setting.isNotiEnabled())
                .isLocEnabled(setting.isLocEnabled())
                .earlyArrivalTime(setting.getEarlyArrivalTime())
                .isReminderActive(setting.isReminderActive())
                .calendarType(setting.getCalendarType())
                .scheduleReminderTimes(scheduleTimes)
                .departureReminderTimes(departureTimes)
                .build();
    }

}
