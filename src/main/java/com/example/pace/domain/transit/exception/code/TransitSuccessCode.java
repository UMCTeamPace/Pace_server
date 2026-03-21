package com.example.pace.domain.transit.exception.code;

import com.example.pace.global.apiPayload.code.BaseSuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum TransitSuccessCode implements BaseSuccessCode {
    TRANSIT_BUS_OK(
            HttpStatus.OK,
            "출발지역 버스 정보를 성공적으로 조회하였습니다.",
            "TRANSIT_BUS200_1"
    ),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final String code;
}
