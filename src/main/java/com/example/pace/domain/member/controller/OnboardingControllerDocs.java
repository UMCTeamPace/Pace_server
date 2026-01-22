package com.example.pace.domain.member.controller;

import com.example.pace.domain.member.dto.request.OnboardingReqDTO;
import com.example.pace.domain.member.dto.response.OnboardingResDTO;
import com.example.pace.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Onboarding", description = "온보딩 API")
public interface OnboardingControllerDocs {

    @Operation(summary = "온보딩 설정 저장", description = "온보딩에서 입력한 값을 사용자 설정으로 저장합니다.")
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
                                        "onboardingCompleted": true
                                      }
                                    }
                                    """
                    )
            )
    )
    ApiResponse<OnboardingResDTO> upsertOnboarding(
            @RequestParam Long memberId,
            @RequestBody OnboardingReqDTO request
    );
}
