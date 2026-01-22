package com.example.pace.domain.member.exception;

import com.example.pace.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OnboardingSuccessCode implements BaseSuccessCode {


    ONBOARDING_SUCCESS(
            HttpStatus.OK,
            "ONBOARDING200_1",
            "온보딩 설정 저장 성공"
    ),

    ONBOARDING_STATUS_SUCCESS(
            HttpStatus.OK,
            "ONBOARDING200_2",
            "온보딩 상태 조회 성공"
    );

    private final HttpStatus httpStatus;
    private final String message;
    private final String code;
}
