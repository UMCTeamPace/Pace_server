package com.example.pace.domain.schedule.exception.code;

import com.example.pace.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@Getter
public enum ScheduleErrorCode implements BaseErrorCode {

    SCHEDULE_CANNOT_REPEAT_WITH_PATH(HttpStatus.BAD_REQUEST, "SCHEDULE400_1", "경로일정은 반복이 불가능합니다."),

    INVALID_TIME_RANGE(HttpStatus.BAD_REQUEST, "SCHEDULE400_2", "시작 시간이 종료 시간보다 늦을 수 없습니다"),

    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "SCHEDULE404_1", "해당 일정을 찾을 수 없습니다."),

    SCHEDULE_FORBIDDEN(HttpStatus.FORBIDDEN, "SCHEDULE403_1", "해당 일정에 대한 권한이 없습니다."),

    ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "SCHEDULE404_2", "해당 일정에 등록된 경로가 없습니다."),

    NOT_PATH_SCHEDULE(HttpStatus.BAD_REQUEST, "SCHEDULE400_3", "경로 일정이 아닙니다.");


    private final HttpStatus httpStatus;
    private final String message;
    private final String code;
}
