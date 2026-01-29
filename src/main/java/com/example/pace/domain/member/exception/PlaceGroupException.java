package com.example.pace.domain.member.exception;

import com.example.pace.global.apiPayload.code.BaseErrorCode;
import com.example.pace.global.apiPayload.exception.GeneralException;

public class PlaceGroupException extends GeneralException {
    public PlaceGroupException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
