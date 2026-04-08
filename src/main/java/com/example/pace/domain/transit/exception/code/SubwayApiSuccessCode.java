package com.example.pace.domain.transit.exception.code;

import com.example.pace.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum SubwayApiSuccessCode implements BaseSuccessCode {
    SUBWAY_API_SUCCESS(
            HttpStatus.OK,
            "지하철 도착 정보를 성공적으로 조회하였습니다.",
            "SUBWAY_API200_1"
    ),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final String code;
}
