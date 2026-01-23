package com.example.pace.domain.member.exception;

import com.example.pace.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OnboardingErrorCode implements BaseErrorCode{

    ONBOARDING_NOT_FOUND(HttpStatus.NOT_FOUND, "온보딩 정보를 찾을 수 없습니다.", "ONBOARDING404_1"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다.", "ONBOARDING401_1"),
    FORBIDDEN (HttpStatus.FORBIDDEN, "접근 권한이 없습니다.", "ONBOARDING403_1"),
    ONBOARDING_ALREADY_COMPLETED(HttpStatus.CONFLICT, "이미 온보딩이 완료된 사용자입니다.", "ONBOARDING409_1"),
    ONBOARDING_REQUIRED(HttpStatus.CONFLICT, "온보딩이 필요합니다.", "ONBOARDING409_2"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "ONBOARDING404_2", "회원 정보를 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String message;
    private final String code;



}