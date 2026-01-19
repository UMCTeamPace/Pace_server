package com.example.pace.domain.auth.converter;

import com.example.pace.domain.auth.dto.response.AuthResDTO;
import com.example.pace.domain.member.entity.Member;

public class AuthConverter {
    public static AuthResDTO.ExistingUserDTO toExistingUserDTO(
            Member member,
            String accessToken,
            String refreshToken
    ) {
        // 기존 회원용 dto 변환
        return AuthResDTO.ExistingUserDTO.builder()
                .memberId(member.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }

    public static AuthResDTO.NewUserDTO toNewUserDTO(Long memberId, String email, String tempToken) {
        return AuthResDTO.NewUserDTO.builder()
                .memberId(memberId)
                .email(email)
                .tempToken(tempToken)
                .build();
    }
}
