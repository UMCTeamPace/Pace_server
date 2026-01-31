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
    PLACE_GROUP_DUPLICATE_GROUP_NAME(
            HttpStatus.BAD_REQUEST,
            "중복된 그룹 이름입니다.",
            "PLACE_GROUP400_1"
    ),
    PLACE_GROUP_UNAUTHORIZED(
            HttpStatus.UNAUTHORIZED,
            "소유하지 않은 그룹에 접근하려 했습니다.",
            "PLACE_GROUP401_1"
    ),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final String code;
}
