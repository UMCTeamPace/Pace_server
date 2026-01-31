package com.example.pace.domain.member.service;

import com.example.pace.domain.member.converter.SettingConverter;
import com.example.pace.domain.member.dto.request.SettingUpdateRequestDTO;
import com.example.pace.domain.member.dto.response.SettingResponseDTO;
import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.member.entity.ReminderTime;
import com.example.pace.domain.member.entity.Setting;
import com.example.pace.domain.member.enums.AlarmType;
import com.example.pace.domain.member.enums.CalendarType;
import com.example.pace.domain.member.exception.SettingErrorCode;
import com.example.pace.domain.member.exception.SettingException;
import com.example.pace.domain.member.repository.MemberRepository;
import com.example.pace.domain.member.repository.SettingRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettingServiceImpl implements SettingService {

    private final SettingRepository settingRepository;
    private final MemberRepository memberRepository;

    @Override
    public SettingResponseDTO getMySetting(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("member not found"));

        Setting setting = settingRepository.findByMember(member)
                .orElseGet(() -> settingRepository.save(createDefaultSetting(member)));

        return SettingResponseDTO.from(setting);
    }

    @Override
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

        return SettingConverter.toResponse(setting);
    }

    // ReminderTime 생성
    private List<ReminderTime> buildReminderTimes(
            Setting setting,
            AlarmType alarmType,
            List<Integer> minutesList
    ) {
        return minutesList.stream()
                .filter(Objects::nonNull)
                .filter(m -> m > 0)
                .distinct()
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
}
