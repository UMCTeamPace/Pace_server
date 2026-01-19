package com.example.pace.domain.member.settings.service;

import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.member.settings.dto.request.SettingUpdateRequest;
import com.example.pace.domain.member.settings.dto.response.SettingResponse;
import com.example.pace.domain.member.settings.entity.Setting;
import com.example.pace.domain.member.settings.repository.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettingServiceImpl implements SettingService {

    private final SettingRepository settingRepository;

    @Override
    public SettingResponse getMySetting(Member member) {
        Setting setting = settingRepository.findByMember(member)
                .orElseGet(() -> settingRepository.save(Setting.defaultOf(member)));

        return SettingResponse.from(setting);
    }

    @Override
    @Transactional
    public SettingResponse updateMySetting(Member member, SettingUpdateRequest request) {
        Setting setting = settingRepository.findByMember(member)
                .orElseGet(() -> settingRepository.save(Setting.defaultOf(member)));

        setting.update(
                request.getEarlyArrivalTime(),
                request.getIsNotiEnabled(),
                request.getIsLocEnabled(),
                request.getIsReminderActive(),
                request.getDeptReminderFreq(),
                request.getDeptReminderInterval(),
                request.getCalendarType()
        );

        // reminder_time(다중 선택)은 별도 테이블이므로 따로 처리
        // reminder_time (다중 선택) 교체 저장
        // - null이면 "수정 안 함"
        // - 빈 리스트면 "전부 삭제"
        if (request.getReminderTimes() != null) {
            setting.replaceReminderTimes(request.getReminderTimes());
        }

        return SettingResponse.from(setting);
    }
}