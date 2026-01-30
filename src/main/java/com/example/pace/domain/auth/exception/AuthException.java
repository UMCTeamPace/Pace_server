package com.example.pace.domain.auth.exception;

import com.example.pace.global.apiPayload.code.BaseErrorCode;
import com.example.pace.global.apiPayload.exception.GeneralException;

public class AuthException extends GeneralException {
    public AuthException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
