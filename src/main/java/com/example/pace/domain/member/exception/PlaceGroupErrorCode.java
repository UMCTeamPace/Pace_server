package com.example.pace.domain.member.exception;

import com.example.pace.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PlaceGroupErrorCode implements BaseErrorCode {
    PLACE_GROUP_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "조회된 그룹이 없습니다.",
            "PLACE_GROUP404_1"
    ),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final String code;
}
