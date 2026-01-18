package com.example.pace.domain.member.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

// https://kauth.kakao.com/oauth/token 해당 url로 API 호출을 보냈을 때의 응답되는 값(필드)
@Getter
@NoArgsConstructor
public class KakaoTokenResDTO {
    @JsonProperty("token_type")
    private String tokenType; // 토큰 타입, 보통 bearer로 고정된 값을 가짐

    @JsonProperty("access_token")
    private String accessToken; // 사용자 인증 및 API 호출에 사용하는 액세스 토큰

    @JsonProperty("expires_on")
    private Integer expiresIn; // 액세스 토큰 만료 시간(단위: 초)

    @JsonProperty("refresh_token")
    private String refreshToken; // 리프레쉬 토큰스

    @JsonProperty("refresh_token_expires_in")
    private Integer refreshTokenExpiresIn; // 리프레쉬 토큰 만료 시간(초)

    @JsonProperty("scope")
    private String scope; // 사용자 동의 항목 범위
}
