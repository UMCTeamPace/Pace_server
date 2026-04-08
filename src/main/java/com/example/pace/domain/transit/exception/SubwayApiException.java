package com.example.pace.domain.transit.exception;

import com.example.pace.global.apiPayload.code.BaseErrorCode;
import com.example.pace.global.apiPayload.exception.GeneralException;

public class SubwayApiException extends GeneralException {
    public SubwayApiException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
