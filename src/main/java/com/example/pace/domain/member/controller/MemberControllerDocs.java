package com.example.pace.domain.member.controller;

import com.example.pace.global.apiPayload.ApiResponse;
import com.example.pace.global.auth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

public interface MemberControllerDocs {
    @Operation(
            summary = "회원 탈퇴",
            description = "Authorization 헤더에 Bearer {액세스 토큰} 을 넣으면 회원 정보 식별이 되므로, 회원id는 안보내도 됩니다!"
    )
    ApiResponse<String> withdrawal(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    );

    @Operation(
            summary = "로그아웃",
            description = "해당 API 성공 응답을 받으면 안드로이드 앱에서 반드시 기기에 저장된 액세스 토큰을 삭제해야 하고, 카카오 SDK를 통해 카카오 연결을 끊은 후(unlink) "
                    + "로그인 페이지로 넘겨야 합니다."
    )
    ApiResponse<String> logout(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    );
}
