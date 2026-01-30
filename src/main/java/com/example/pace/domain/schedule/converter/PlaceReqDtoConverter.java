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
    // 기존 장소 -> 새 장소 엔티티 객체
    public static Place toPlaceFromExisting(Place existing) {
        if (existing == null) return null;
        return Place.builder()
                .targetName(existing.getTargetName())
                .targetLat(existing.getTargetLat())
                .targetLng(existing.getTargetLng())
                .build();
    }
}
