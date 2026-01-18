package com.example.pace.domain.member.settings.service;

import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.member.settings.dto.request.SettingUpdateRequest;
import com.example.pace.domain.member.settings.dto.response.SettingResponse;

public interface SettingService {
    SettingResponse getMySetting(Member member);
    SettingResponse updateMySetting(Member member, SettingUpdateRequest request);
}
