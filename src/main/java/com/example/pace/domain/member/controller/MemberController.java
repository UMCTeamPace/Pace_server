package com.example.pace.domain.member.controller;

import com.example.pace.domain.member.service.MemberCommandService;
import com.example.pace.global.apiPayload.ApiResponse;
import com.example.pace.global.apiPayload.code.GeneralSuccessCode;
import com.example.pace.global.auth.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController implements MemberControllerDocs {
    private final MemberCommandService memberCommandService;

    @Override
    @DeleteMapping("/withdrawal")
    public ApiResponse<String> withdrawal(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            HttpServletRequest request
    ) {
        Long memberId = customUserDetails.member().getId();
        String accessToken = request.getHeader("Authorization").substring(7);
        memberCommandService.withdrawalMember(memberId, accessToken);

        return ApiResponse.onSuccess(
                GeneralSuccessCode.OK,
                "회원 탈퇴가 완료되었습니다."
        );
    }

    @PostMapping("/logout")
    @Override
    public ApiResponse<String> logout(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            HttpServletRequest request
    ) {
        Long memberId = customUserDetails.member().getId();
        String accessToken = request.getHeader("Authorization").substring(7);
        memberCommandService.logout(memberId, accessToken);

        return ApiResponse.onSuccess(
                GeneralSuccessCode.OK,
                "로그아웃 되었습니다."
        );
    }
}
