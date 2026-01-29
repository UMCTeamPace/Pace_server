package com.example.pace.domain.schedule.exception;

import com.example.pace.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ScheduleSuccessCode implements BaseSuccessCode {

    SCHEDULE_CREATE_OK(HttpStatus.CREATED, "SCHEDULE201_1", "일정이 성공적으로 생성되었습니다."),

    SCHEDULE_GET_OK(HttpStatus.OK, "SCHEDULE200_1", "일정을 성공적으로 조회하였습니다."),

    SCHEDULE_DELETE_OK(HttpStatus.OK, "SCHEDULE200_2", "일정이 성공적으로 삭제되었습니다."),

    SCHEDULE_UPDATE_OK(HttpStatus.OK, "SCHEDULE200_3", "일정이 성공적으로 수정되었습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}