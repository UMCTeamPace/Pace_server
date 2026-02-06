package com.example.pace.domain.schedule.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransitType {

    BUS("bus"),
    SUBWAY("subway"),
    TRAIN("train"),
    UNDEFINED("undefined");

    private final String googleValue;
}