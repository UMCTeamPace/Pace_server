package com.example.pace.domain.member.service.command;

import com.example.pace.domain.member.converter.SettingConverter;
import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.member.entity.ReminderTime;
import com.example.pace.domain.member.entity.Setting;
import com.example.pace.domain.member.enums.AlarmType;
import com.example.pace.domain.member.enums.CalendarType;
import com.example.pace.domain.member.exception.code.SettingErrorCode;
import com.example.pace.domain.member.exception.SettingException;
import com.example.pace.domain.member.repository.SettingRepository;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettingCommandService {
    private final SettingRepository settingRepository;

    private static final int MAX_ALARMS_PER_TYPE = 5;
    private static final Set<Integer> ALLOWED_SCHEDULE_MINUTES = Set.of(
            5, 10, 15, 30, 60, 120,
            1440, 2880, 10080
    );

    private static final Set<Integer> ALLOWED_DEPARTURE_MINUTES = Set.of(
            5, 10, 15, 20, 25, 30, 35, 45, 50, 55, 60
    );

    // ReminderTime 생성
    private List<ReminderTime> buildReminderTimes(
            Setting setting,
            AlarmType alarmType,
            List<Integer> minutesList
    ) {
        if (minutesList == null) {
            return List.of();
        }

        List<Integer> distinct = minutesList.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (distinct.size() > MAX_ALARMS_PER_TYPE) {
            throw new SettingException(SettingErrorCode.TOO_MANY_ALARMS);
        }

        Set<Integer> allowed = (alarmType == AlarmType.SCHEDULE)
                ? ALLOWED_SCHEDULE_MINUTES
                : ALLOWED_DEPARTURE_MINUTES;

        for (Integer m : distinct) {
            if (!allowed.contains(m)) {
                throw new SettingException(SettingErrorCode.INVALID_ALARM_MINUTES);
            }
        }

        return distinct.stream()
                .map(m -> SettingConverter.toEntity(setting, alarmType, m))
                .toList();
    }


    // 기본 Setting 생성
    private Setting createDefaultSetting(Member member) {
        return Setting.builder()
                .member(member)
                .isNotiEnabled(false)
                .isLocEnabled(false)
                .earlyArrivalTime(20)
                .isReminderActive(true)
                .calendarType(CalendarType.GOOGLE)
                .build();
    }

    @Transactional
    public Setting getOrCreateSetting(Member member) {
        return settingRepository.findByMember(member)
                .orElseGet(() -> settingRepository.save(createDefaultSetting(member)));
    }

    @Transactional
    public void replaceReminderTimesFromMinutes(Setting setting, AlarmType type, List<Integer> minutesList) {
        List<Integer> safe = (minutesList == null) ? List.of() : minutesList;

        setting.replaceReminderTimes(
                type,
                buildReminderTimes(setting, type, safe)
        );
    }
}
