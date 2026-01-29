package com.example.pace.domain.member.exception;

import com.example.pace.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SavedPlaceErrorCode implements BaseErrorCode {
    SAVED_PLACE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST,
            "이미 해당 그룹 내에 동일한 장소가 있습니다.",
            "PLACE400_1"),
    SAVED_PLACE_NOT_EXISTS_GROUP_NAME(
            HttpStatus.BAD_REQUEST,
            "그룹명이 없습니다.",
            "PLACE400_2"
    ),
    SAVED_PLACE_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "조회된 장소가 없습니다.",
            "PLACE404_1"
    );

    private final HttpStatus httpStatus;
    private final String message;
    private final String code;
}
