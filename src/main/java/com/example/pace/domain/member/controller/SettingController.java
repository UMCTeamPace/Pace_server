package com.example.pace.domain.member.controller;
import com.example.pace.domain.member.dto.request.SettingUpdateRequest;
import com.example.pace.domain.member.dto.response.SettingResponse;
import com.example.pace.domain.member.exception.SettingSuccessCode;
import com.example.pace.domain.member.service.SettingService;
import com.example.pace.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Settings", description = "사용자 설정 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member/settings")
public class SettingController implements SettingControllerDocs{

    private final SettingService settingService;

    @GetMapping
    @Override
    public ApiResponse<SettingResponse> getMySetting (@RequestParam Long memberId) {
        return ApiResponse.onSuccess(
                SettingSuccessCode.SETTING_GET_OK,
                settingService.getMySetting(memberId)
        );
        // return settingService.getMySetting(memberId);
    }

    @Override
    @PatchMapping
    public ApiResponse<SettingResponse> updateMySetting(
            @RequestParam Long memberId,
            @org.springframework.web.bind.annotation.RequestBody SettingUpdateRequest request
    ) {
        return ApiResponse.onSuccess(
            SettingSuccessCode.SETTING_UPDATE_OK,
            settingService.updateMySetting(memberId, request)
        );
    }
}

