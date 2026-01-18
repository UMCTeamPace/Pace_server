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
                request.getIsLocEnabled()
        );

        return SettingResponse.from(setting);
    }
}