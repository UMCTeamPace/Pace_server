package com.example.pace.domain.auth.controller;

import com.example.pace.domain.auth.dto.request.AuthReqDTO;
import com.example.pace.domain.auth.dto.response.AuthResDTO;
import com.example.pace.domain.auth.exception.AuthSuccessCode;
import com.example.pace.domain.auth.service.AuthCommandService;
import com.example.pace.domain.member.exception.MemberSuccessCode;
import com.example.pace.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthCommandService authCommandService;

    @PostMapping("/kakao")
    public ApiResponse<AuthResDTO.LoginResultDTO> kakaoLogin(
            @RequestBody AuthReqDTO.KakaoLoginRequestDTO request
    ) {
        return ApiResponse.onSuccess(
                MemberSuccessCode.MEMBER_LOGIN_OK,
                authCommandService.loginWithKakao(request.getAccessToken())
        );
    }

    @PostMapping("/reissue")
    public ApiResponse<AuthResDTO.LoginResultDTO> reissueToken(
            @RequestBody AuthReqDTO.ReissueRequestDTO request
    ) {
        return ApiResponse.onSuccess(
                AuthSuccessCode.AUTH_REISSUE_OK,
                authCommandService.reissueToken(request.getRefreshToken())
        );
    }
}
