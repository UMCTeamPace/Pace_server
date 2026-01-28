package com.example.pace.domain.schedule.exception;

import com.example.pace.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ScheduleSuccessCode implements BaseSuccessCode{

    SCHEDULE_ROUTE_UPDATE_OK(HttpStatus.OK, "일정에 경로 수정 성공", "SCHEDULE200_1");

    private final HttpStatus httpStatus;
    private final String message;
    private final String code;
}