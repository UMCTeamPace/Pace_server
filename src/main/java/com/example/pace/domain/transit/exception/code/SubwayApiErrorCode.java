package com.example.pace.domain.transit.exception.code;

import com.example.pace.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum SubwayApiErrorCode implements BaseErrorCode {
    SUBWAY_API_4XX(
            HttpStatus.BAD_REQUEST,
            "지하철 도착 정보 API 호출 간 클라이언트 에러가 발생하였습니다.",
            "SUBWAY_API400_1"
    ),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final String code;
}
