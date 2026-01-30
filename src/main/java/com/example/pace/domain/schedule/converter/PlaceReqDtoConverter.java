package com.example.pace.domain.schedule.converter;

import com.example.pace.domain.schedule.dto.request.ScheduleReqDto;
import com.example.pace.domain.schedule.entity.Place;



public class PlaceReqDtoConverter {
    // 요청dto -> Place 엔티티 객체
    public static Place toPlace(ScheduleReqDto.PlaceDto placeDto) {
        return  Place.builder()
                .targetName(placeDto.getTargetName())
                .targetLat(placeDto.getTargetLat())
                .targetLng(placeDto.getTargetLng())
                .build();
    }
}
