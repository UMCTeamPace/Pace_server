package com.example.pace.domain.member.exception;

import com.example.pace.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SettingErrorCode implements BaseErrorCode {

    SETTING_NOT_FOUND(HttpStatus.NOT_FOUND, "SETTING404_1", "설정 정보를 찾을 수 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "SETTING401_1", "인증이 필요합니다."),
    ONBOARDING_REQUIRED(HttpStatus.CONFLICT, "SETTING409_1", "온보딩 후에 가능합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "SETTING403_1", "접근 권한이 없습니다."),
    TOO_MANY_ALARMS(HttpStatus.BAD_REQUEST, "SETTING400_1", "알람은 타입별로 최대 5개까지 설정할 수 있습니다."),
    INVALID_ALARM_MINUTES(HttpStatus.BAD_REQUEST, "SETTING400_2", "허용되지 않은 알람 시간 값이 포함되어 있습니다.");

    private final HttpStatus httpStatus;
    private final String message;
    private final String code;

}
