package com.example.pace.global.apiPayload.exception;

import com.example.pace.global.apiPayload.code.BaseErrorCode;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {
    private final BaseErrorCode code;

    public GeneralException(BaseErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }
}
