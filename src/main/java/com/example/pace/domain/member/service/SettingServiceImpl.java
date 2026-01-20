package com.example.pace.domain.member.service;

import com.example.pace.domain.member.converter.SettingConverter;
import com.example.pace.domain.member.dto.request.SettingUpdateRequestDTO;
import com.example.pace.domain.member.dto.response.SettingResponseDTO;
import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.member.entity.Setting;
import com.example.pace.domain.member.enums.AlarmType;
import com.example.pace.domain.member.exception.SettingErrorCode;
import com.example.pace.domain.member.exception.SettingException;
import com.example.pace.domain.member.repository.MemberRepository;
import com.example.pace.domain.member.repository.SettingRepository;
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
                .orElseGet(() -> settingRepository.save(Setting.defaultOf(member)));

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


        // 일정 알림
        if (request.getScheduleReminderTimes() != null) {
            setting.replaceReminderTimes(AlarmType.SCHEDULE, request.getScheduleReminderTimes());
        }

        // 출발 알림
        if (request.getDepartureReminderTimes() != null) {
            setting.replaceReminderTimes(AlarmType.DEPARTURE, request.getDepartureReminderTimes());
        }

        return SettingConverter.toResponse(setting);
    }
}