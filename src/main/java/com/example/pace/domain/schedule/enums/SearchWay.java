package com.example.pace.domain.schedule.enums;

import lombok.Getter;

@Getter
public enum SearchWay {
    // 분기처리를 위해 데이터 매핑
    FASTEST(null),                 // 기본 (구글에 안 보냄)
    MIN_TRANSFER("fewer_transfers"),
    MIN_WALK("less_walking");

    private final String googleValue;

    SearchWay(String googleValue) {
        this.googleValue = googleValue;

    }
}