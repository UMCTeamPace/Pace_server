package com.example.pace.domain.member.exception;

import com.example.pace.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PlaceGroupSuccessCode implements BaseSuccessCode {
    PLACE_GROUP_FOUND_OK(
            HttpStatus.OK,
            "그룹이 성공적으로 조회되었습니다.",
            "PLACE_GROUP200_1"
    ),
    ;
    private final HttpStatus httpStatus;
    private final String message;
    private final String code;
}
