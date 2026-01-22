package com.example.pace.domain.auth.controller;

import com.example.pace.domain.auth.dto.request.AuthReqDTO;
import com.example.pace.domain.auth.dto.response.AuthResDTO;
import com.example.pace.domain.auth.dto.response.AuthResDTO.LoginResultDTO;
import com.example.pace.global.apiPayload.ApiResponse;
import com.example.pace.global.auth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthControllerDocs {
    @Operation(
            summary = "카카오 소셜 로그인",
            description = "카카오 액세스 토큰을 보내면 회원 정보를 반환합니다."
    )
    @Parameters({
            @Parameter(name = "kakaoAccessToken", description = "카카오로부터 받은 액세스 토큰입니다.")
    })
    ApiResponse<AuthResDTO.LoginResultDTO> kakaoLogin(
            @RequestBody AuthReqDTO.KakaoLoginRequestDTO request
    );

    @Operation(
            summary = "액세스 토큰 재발행",
            description = "리프레쉬 토큰을 보내면 새 액세스 토큰을 재발행합니다."
    )
    @Parameters({
            @Parameter(name = "refreshToken", description = "리프레쉬 토큰입니다.")
    })
    ApiResponse<LoginResultDTO> reissueToken(
            @RequestBody AuthReqDTO.ReissueRequestDTO request
    );
}
