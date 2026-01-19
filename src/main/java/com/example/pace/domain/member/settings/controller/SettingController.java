package com.example.pace.domain.member.settings.controller;

import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.member.settings.dto.request.SettingUpdateRequest;
import com.example.pace.domain.member.settings.dto.response.SettingResponse;
import com.example.pace.domain.member.settings.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member/settings")
public class SettingController {

    private final SettingService settingService;

                                 

    @GetMapping
    public SettingResponse getMySetting(@AuthenticationPrincipal Member member) {
        return settingService.getMySetting(member);
    }


    @PatchMapping
    public SettingResponse updateMySetting(
            @AuthenticationPrincipal Member member,
            @RequestBody SettingUpdateRequest request
    ) {
        return settingService.updateMySetting(member, request);
    }
}
