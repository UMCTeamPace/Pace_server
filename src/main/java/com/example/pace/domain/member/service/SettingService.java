package com.example.pace.domain.member.service;

import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.member.dto.request.SettingUpdateRequest;
import com.example.pace.domain.member.dto.response.SettingResponse;

public interface SettingService {
    SettingResponse getMySetting(Long memberId);
    SettingResponse updateMySetting(Long memberId, SettingUpdateRequest request);
}
