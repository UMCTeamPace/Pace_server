package com.example.pace.domain.schedule.enums;


public enum SearchWay {
    FASTEST,       // 최적경로 ->추후 duration 내림차순하여 최소시간 설정
    MIN_TRANSFER,  // 최소환승
    MIN_WALK       // 최소도보
}