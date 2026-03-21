package com.example.pace.domain.transit.exception.code;

import com.example.pace.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum TransitErrorCode implements BaseErrorCode {
    TRANSIT_BUS_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "조회된 버스 정보가 없습니다.",
            "TRANSIT_BUS404_1"
    ),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final String code;
}
