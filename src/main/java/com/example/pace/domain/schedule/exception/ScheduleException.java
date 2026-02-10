package com.example.pace.domain.schedule.exception;

import com.example.pace.domain.schedule.exception.code.ScheduleErrorCode;

public class ScheduleException extends RuntimeException {
    private final ScheduleErrorCode errorCode;

    public ScheduleException(ScheduleErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
