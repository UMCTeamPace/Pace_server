package com.example.pace.domain.member.exception;

import com.example.pace.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SavedPlaceSuccessCode implements BaseSuccessCode {
    SAVED_PLACE_CREATE_OK(
            HttpStatus.CREATED,
            "장소가 성공적으로 저장되었습니다.",
            "PLACE201_1"
    ),
    SAVED_PLACE_FOUND_OK(
            HttpStatus.OK,
            "장소가 성공적으로 조회되었습니다.",
            "PLACE200_1"
    ),
    SAVED_PLACE_DELETE_OK(
            HttpStatus.OK,
            "장소가 성공적으로 삭제되었습니다.",
            "PLACE200_2"
    ),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final String code;
}
