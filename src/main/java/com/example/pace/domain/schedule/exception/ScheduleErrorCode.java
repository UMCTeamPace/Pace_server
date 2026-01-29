package com.example.pace.domain.schedule.exception;

import com.example.pace.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ScheduleErrorCode implements BaseErrorCode {

    SCHEDULE_CANNOT_REPEAT_WITH_PATH(HttpStatus.BAD_REQUEST, "SCHEDULE400_1", "경로일정은 반복이 불가능합니다."),

    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "SCHEDULE404_1", "해당 일정을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}