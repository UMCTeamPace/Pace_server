package com.example.pace.domain.schedule.exception;

import com.example.pace.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ScheduleSuccessCode implements BaseSuccessCode{

    SCHEDULE_CREATE_OK(HttpStatus.CREATED, "일정이 성공적으로 생성되었습니다.", "SCHEDULE201_1"),

    SCHEDULE_GET_OK(HttpStatus.OK, "일정을 성공적으로 조회하였습니다.", "SCHEDULE200_2"),

    SCHEDULE_DELETE_OK(HttpStatus.OK, "일정이 성공적으로 삭제되었습니다.", "SCHEDULE200_3"),

    SCHEDULE_UPDATE_OK(HttpStatus.OK, "일정이 성공적으로 수정되었습니다.", "SCHEDULE200_4"),

    SCHEDULE_ROUTE_UPDATE_OK(HttpStatus.OK, "일정에 경로 수정 성공", "SCHEDULE200_4"),

    SCHEDULE_ROUTE_DELETE_OK(HttpStatus.OK, "일정에 경로 삭제 성공", "SCHEDULE200_5"),

    SCHEDULE_CONVERTED(HttpStatus.OK, "경로 일정을 일반 일정으로 전환했습니다.", "SCHEDULE200_6");

    private final HttpStatus httpStatus;
    private final String message;
    private final String code;
}