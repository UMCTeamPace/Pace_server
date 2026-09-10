package com.example.pace.domain.weather.exception;

import com.example.pace.domain.weather.exception.code.WeatherErrorCode;
import com.example.pace.global.apiPayload.exception.GeneralException;

public class WeatherException extends GeneralException {
    public WeatherException(WeatherErrorCode errorCode) {
        super(errorCode);
    }
}
