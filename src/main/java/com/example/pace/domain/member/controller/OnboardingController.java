package com.example.pace.domain.member.controller;

import com.example.pace.domain.member.dto.request.OnboardingReqDTO;
import com.example.pace.domain.member.dto.response.OnboardingResDTO;
import com.example.pace.domain.member.exception.OnboardingSuccessCode;
import com.example.pace.domain.member.service.OnboardingService;
import com.example.pace.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class OnboardingController implements OnboardingControllerDocs {

    private final OnboardingService onboardingService;

    @Override
    @PutMapping("/onboarding")
    public ApiResponse<OnboardingResDTO> upsertOnboarding(
            @RequestParam Long memberId,
            @Valid @RequestBody OnboardingReqDTO request
    ) {
        OnboardingResDTO result = onboardingService.upsertOnboarding(memberId, request);
        return ApiResponse.onSuccess(OnboardingSuccessCode.ONBOARDING_SUCCESS, result);
    }
}
