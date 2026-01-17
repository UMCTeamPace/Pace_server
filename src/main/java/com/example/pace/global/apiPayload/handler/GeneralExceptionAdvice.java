package com.example.pace.global.apiPayload.handler;

import com.example.pace.global.apiPayload.ApiResponse;
import com.example.pace.global.apiPayload.code.BaseErrorCode;
import com.example.pace.global.apiPayload.code.GeneralErrorCode;
import com.example.pace.global.apiPayload.exception.GeneralException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GeneralExceptionAdvice {
    // 커스텀 예외 처리
    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ApiResponse<Void>> handleException(GeneralException e) {
        return ResponseEntity
                .status(e.getCode().getHttpStatus())
                .body(ApiResponse.onFailure(
                        e.getCode()
                ));
    }

    // 그 외의 정의되지 않은 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception e) {
        BaseErrorCode code = GeneralErrorCode.INTERNAL_SERVER_ERROR;

        return ResponseEntity
                .status(code.getHttpStatus())
                .body(ApiResponse.onFailure(
                        code,
                        e.getMessage()
                ));
    }
}
