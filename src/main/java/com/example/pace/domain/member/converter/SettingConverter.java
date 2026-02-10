package com.example.pace.domain.member.converter;

import com.example.pace.domain.member.dto.response.SettingResponseDTO;
import com.example.pace.domain.member.entity.ReminderTime;
import com.example.pace.domain.member.entity.Setting;
import com.example.pace.domain.member.enums.AlarmType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SettingConverter {

    private SettingConverter() {
    }

    public static ReminderTime toEntity(
            Setting setting,
            AlarmType alarmType,
            Integer minutes
    ) {
        return ReminderTime.builder()
                .setting(setting)
                .alarmType(alarmType)
                .minutes(minutes)
                .build();
    }

    public static SettingResponseDTO toResponse(Setting setting) {

        Map<AlarmType, List<Integer>> timesByType = setting.getReminderTimes().stream()
                .collect(Collectors.groupingBy(
                        ReminderTime::getAlarmType,
                        Collectors.mapping(ReminderTime::getMinutes, Collectors.toList())
                ));

        // REQUIRED_TYPES 보정(없으면 빈 리스트)
        List<SettingResponseDTO.AlarmConfig> alarms = List.of(
                new SettingResponseDTO.AlarmConfig(
                        AlarmType.SCHEDULE,
                        timesByType.getOrDefault(AlarmType.SCHEDULE, List.of())
                ),
                new SettingResponseDTO.AlarmConfig(
                        AlarmType.DEPARTURE,
                        timesByType.getOrDefault(AlarmType.DEPARTURE, List.of())
                )
        );

        return SettingResponseDTO.builder()
                .settingId(setting.getSettingId())
                .earlyArrivalTime(setting.getEarlyArrivalTime())
                .isNotiEnabled(setting.isNotiEnabled())
                .isLocEnabled(setting.isLocEnabled())
                .earlyArrivalTime(setting.getEarlyArrivalTime())
                .isReminderActive(setting.isReminderActive())
                .calendarType(setting.getCalendarType())
                .alarms(alarms)
                .createdAt(setting.getCreatedAt())
                .updatedAt(setting.getUpdatedAt())
                .build();
    }
}
