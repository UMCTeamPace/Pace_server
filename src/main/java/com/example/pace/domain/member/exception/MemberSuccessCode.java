package com.example.pace.domain.member.exception;

import com.example.pace.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberSuccessCode implements BaseSuccessCode {
    MEMBER_LOGIN_OK(HttpStatus.OK,
            "성공적으로 로그인에 성공하였습니다.",
            "MEMBER200_1"
    ),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final String code;
}
