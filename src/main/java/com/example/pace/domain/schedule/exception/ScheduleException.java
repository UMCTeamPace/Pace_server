package com.example.pace.domain.schedule.exception;

public class ScheduleException extends RuntimeException {
    private final ScheduleErrorCode errorCode;

    public ScheduleException(ScheduleErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
