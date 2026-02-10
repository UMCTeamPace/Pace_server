package com.example.pace.domain.member.controller;

import com.example.pace.domain.member.controller.docs.SettingControllerDocs;
import com.example.pace.domain.member.dto.request.SettingUpdateRequestDTO;
import com.example.pace.domain.member.dto.response.SettingResponseDTO;
import com.example.pace.domain.member.exception.code.SettingSuccessCode;
import com.example.pace.domain.member.service.command.SettingCommandService;
import com.example.pace.global.apiPayload.ApiResponse;
import com.example.pace.global.auth.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Settings", description = "사용자 설정 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member/settings")
public class SettingController implements SettingControllerDocs {

    private final SettingCommandService settingService;

    @GetMapping
    @Override
    public ApiResponse<SettingResponseDTO> getMySetting(@AuthenticationPrincipal CustomUserDetails user) {
        return ApiResponse.onSuccess(
                SettingSuccessCode.SETTING_GET_OK,
                settingService.getMySetting(user.member().getId())
        );
    }

    @Override
    @PatchMapping
    public ApiResponse<SettingResponseDTO> updateMySetting(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody SettingUpdateRequestDTO request
    ) {
        return ApiResponse.onSuccess(
                SettingSuccessCode.SETTING_UPDATE_OK,
                settingService.updateMySetting(user.member().getId(), request)
        );
    }
}

