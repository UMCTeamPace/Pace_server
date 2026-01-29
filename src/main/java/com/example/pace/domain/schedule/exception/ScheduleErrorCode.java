package com.example.pace.domain.schedule.exception;

import com.example.pace.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ScheduleErrorCode implements BaseErrorCode {

    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다.", "SCHEDULE404_1"),
    SCHEDULE_FORBIDDEN(HttpStatus.FORBIDDEN, "해당 일정에 대한 권한이 없습니다.", "SCHEDULE404_2"),
    ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 일정에 등록된 경로가 없습니다.", "SCHEDULE404_3");

    private final HttpStatus httpStatus;
    private final String message;
    private final String code;
}
