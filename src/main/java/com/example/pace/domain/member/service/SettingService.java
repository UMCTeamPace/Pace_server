package com.example.pace.domain.member.service;

import com.example.pace.domain.member.dto.request.SettingUpdateRequestDTO;
import com.example.pace.domain.member.dto.response.SettingResponseDTO;

public interface SettingService {
    SettingResponseDTO getMySetting(Long memberId);
    SettingResponseDTO updateMySetting(Long memberId, SettingUpdateRequestDTO request);
}
