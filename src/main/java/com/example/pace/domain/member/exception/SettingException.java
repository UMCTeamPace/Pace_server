package com.example.pace.domain.member.exception;

import com.example.pace.global.apiPayload.code.BaseErrorCode;
import com.example.pace.global.apiPayload.exception.GeneralException;

public class SettingException extends GeneralException {
    public SettingException(BaseErrorCode errorCode) {super(errorCode);}
}
