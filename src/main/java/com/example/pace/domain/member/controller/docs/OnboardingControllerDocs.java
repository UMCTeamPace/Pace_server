package com.example.pace.domain.member.controller.docs;

import com.example.pace.domain.member.dto.request.OnboardingReqDTO;
import com.example.pace.domain.member.dto.response.OnboardingResDTO;
import com.example.pace.global.apiPayload.ApiResponse;
import com.example.pace.global.auth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Onboarding", description = "온보딩 API")
public interface OnboardingControllerDocs {

    @Operation(summary = "온보딩 설정 저장 및 완료", description = "온보딩 정보를 저장하고, 회원을 정회원(ROLE_USER)으로 승격시킨 후 정식 토큰을 발급합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "온보딩 설정 저장 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = OnboardingResDTO.class),
                    examples = @ExampleObject(
                            name = "성공 응답 예시",
                            value = """
                                    {
                                      "isSuccess": true,
                                      "code": "ONBOARDING200_1",
                                      "message": "온보딩 설정 저장 성공",
                                      "result": {
                                        "onboarding_completed": true,
                                        "access_token": "eyJhbGciOiJIUzI1NiJ9.eyJ...",
                                        "refresh_token": "eyJhbGciOiJIUzI1NiJ9.eyJ...",
                                        "early_arrival_time": 10,
                                        "is_reminder_active": true,
                                        "role": "ROLE_USER"
                                      }
                                    }
                                    """
                    )
            )
    )
    ApiResponse<OnboardingResDTO> upsertOnboarding(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody OnboardingReqDTO request
    );
}
