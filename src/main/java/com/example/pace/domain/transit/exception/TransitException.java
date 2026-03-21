package com.example.pace.domain.transit.exception;

import com.example.pace.global.apiPayload.code.BaseErrorCode;
import com.example.pace.global.apiPayload.exception.GeneralException;

public class TransitException extends GeneralException {
    public TransitException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
