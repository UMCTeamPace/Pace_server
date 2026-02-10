package com.example.pace.domain.member.exception.code;

import com.example.pace.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,
            "조회된 회원이 없습니다.",
            "MEMBER404_1"),
    MEMBER_NOT_ACTIVE(HttpStatus.UNAUTHORIZED,
            "탈퇴한 회원입니다.",
            "MEMBER401_1"),
    MEMBER_UNAUTHORIZED(
            HttpStatus.UNAUTHORIZED,
            "권한이 없습니다.",
            "MEMBER401_2"
    ),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final String code;
}
