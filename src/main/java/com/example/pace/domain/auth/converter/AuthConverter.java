package com.example.pace.domain.auth.converter;

import com.example.pace.domain.auth.dto.response.AuthResDTO;
import com.example.pace.domain.member.entity.Member;

public class AuthConverter {
    public static AuthResDTO.LoginResultDTO toExistingMemberDTO(
            Member member,
            String accessToken,
            String refreshToken
    ) {
        return AuthResDTO.LoginResultDTO.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .isNewUser(false)
                .role(member.getRole())
                .build();
    }

    public static AuthResDTO.LoginResultDTO toNewMemberDTO(
            Member member,
            String tempToken
    ) {
        return AuthResDTO.LoginResultDTO.builder()
                .tempToken(tempToken)
                .email(member.getEmail())
                .nickname(member.getNickname())
                .isNewUser(true)
                .role(member.getRole())
                .build();
    }
}
