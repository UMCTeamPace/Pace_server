package com.example.pace.domain.member.exception;

import com.example.pace.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum SettingSuccessCode implements BaseSuccessCode {
    SETTING_GET_OK(
            HttpStatus.OK,
            "설정 조회에 성공하였습니다.",
            "SETTING200_1"
    ),
    SETTING_UPDATE_OK(
            HttpStatus.OK,
            "설정 수정에 성공했습니다.",
            "SETTING200_2")
    ;


    private final HttpStatus httpStatus;
    private final String message;
    private final String code;
}
