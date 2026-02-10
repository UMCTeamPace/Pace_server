package com.example.pace.domain.member.service.command;

import com.example.pace.domain.member.converter.SettingConverter;
import com.example.pace.domain.member.dto.request.SettingUpdateRequestDTO;
import com.example.pace.domain.member.dto.response.SettingResponseDTO;
import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.member.entity.ReminderTime;
import com.example.pace.domain.member.entity.Setting;
import com.example.pace.domain.member.enums.AlarmType;
import com.example.pace.domain.member.enums.CalendarType;
import com.example.pace.domain.member.exception.code.SettingErrorCode;
import com.example.pace.domain.member.exception.SettingException;
import com.example.pace.domain.member.repository.MemberRepository;
import com.example.pace.domain.member.repository.SettingRepository;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SettingCommandService {
    private final SettingRepository settingRepository;
    private final MemberRepository memberRepository;

    private static final int MAX_ALARMS_PER_TYPE = 5;
    private static final Set<Integer> ALLOWED_SCHEDULE_MINUTES = Set.of(
            0, 5, 10, 15, 30, 60, 120,
            1440, //1일
            2880, //2일
            10080 //1주
    );

    private static final Set<Integer> ALLOWED_DEPARTURE_MINUTES = Set.of(
            0, 5, 10, 15, 20, 25, 30, 35, 45, 50, 55, 60
    );

    @Transactional(readOnly = true)
    public SettingResponseDTO getMySetting(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("member not found"));

        Setting setting = settingRepository.findByMember(member)
                .orElseGet(() -> settingRepository.save(createDefaultSetting(member)));

        return SettingResponseDTO.from(setting);
    }



    public SettingResponseDTO updateMySetting(Long memberId, SettingUpdateRequestDTO request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("member not found"));

        Setting setting = settingRepository.findByMember(member)
                .orElseThrow(() -> new SettingException(SettingErrorCode.SETTING_NOT_FOUND));

        setting.update(
                request.getEarlyArrivalTime(),
                request.getIsNotiEnabled(),
                request.getIsLocEnabled(),
                request.getIsReminderActive(),
                request.getCalendarType()
        );

        if (request.getAlarms() != null) {
            applyAlarms(setting, request.getAlarms());
        }

        return SettingConverter.toResponse(setting);
    }

    public void applyAlarms(Setting setting, List<SettingUpdateRequestDTO.Alarm> alarms) {
        for (SettingUpdateRequestDTO.Alarm alarm : alarms) {
            if (alarm == null || alarm.getType() == null) continue;

            List<Integer> minutes = alarm.getMinutes();

            // minutes == null 이면 "이 타입은 변경하지 않음"
            if (minutes == null) continue;

            // minutes == [] 이면 buildReminderTimes -> [] 이고 replaceReminderTimes가 삭제만 수행
            setting.replaceReminderTimes(
                    alarm.getType(),
                    buildReminderTimes(setting, alarm.getType(), minutes)
            );
        }
    }

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
    
    public SettingResponseDTO getMySetting(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("member not found"));

        Setting setting = settingRepository.findByMember(member)
                .orElseGet(() -> settingRepository.save(createDefaultSetting(member)));

        return SettingResponseDTO.from(setting);
    }

    @Transactional
    public SettingResponseDTO updateMySetting(Long memberId, SettingUpdateRequestDTO request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("member not found"));

        Setting setting = settingRepository.findByMember(member)
                .orElseThrow(() -> new SettingException(SettingErrorCode.SETTING_NOT_FOUND));

        setting.update(
                request.getEarlyArrivalTime(),
                request.getIsNotiEnabled(),
                request.getIsLocEnabled(),
                request.getIsReminderActive(),
                request.getCalendarType()
        );

        if (request.getAlarms() != null) {
            applyAlarms(setting, request.getAlarms());
        } else {
            // 2) alarms가 없으면 기존 방식 유지
            if (request.getScheduleReminderTimes() != null) {
                setting.replaceReminderTimes(
                        AlarmType.SCHEDULE,
                        buildReminderTimes(setting, AlarmType.SCHEDULE, request.getScheduleReminderTimes())
                );
            }

            if (request.getDepartureReminderTimes() != null) {
                setting.replaceReminderTimes(
                        AlarmType.DEPARTURE,
                        buildReminderTimes(setting, AlarmType.DEPARTURE, request.getDepartureReminderTimes())
                );
            }
        }

        return SettingConverter.toResponse(setting);
    }

    public void applyAlarms(Setting setting, List<SettingUpdateRequestDTO.Alarm> alarms) {
        for (SettingUpdateRequestDTO.Alarm alarm : alarms) {
            if (alarm == null || alarm.getType() == null) {
                continue;
            }

            List<Integer> minutes = alarm.getMinutes();

            // minutes == null 이면 "이 타입은 변경하지 않음"
            if (minutes == null) {
                continue;
            }

            // minutes == [] 이면 buildReminderTimes -> [] 이고 replaceReminderTimes가 삭제만 수행
            setting.replaceReminderTimes(
                    alarm.getType(),
                    buildReminderTimes(setting, alarm.getType(), minutes)
            );
        }
    }
}
