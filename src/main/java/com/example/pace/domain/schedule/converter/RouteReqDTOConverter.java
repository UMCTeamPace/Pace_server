package com.example.pace.domain.schedule.converter;

import com.example.pace.domain.schedule.dto.request.RouteSaveReqDto;
import com.example.pace.domain.schedule.entity.Route;

public class RouteReqDTOConverter {
    //요청 dto->엔티티
    public static Route toRoute(RouteSaveReqDto.CreateRouteDTO createRouteDTO) {
        return Route.builder()
                .originLat(createRouteDTO.originLat())
                .originLng(createRouteDTO.originLng())
                .destLat(createRouteDTO.destLat())
                .destLng(createRouteDTO.destLng())
                .arrivalTime(createRouteDTO.arrivalTime())
                .departureTime(createRouteDTO.departureTime())
                .build();
    }
}
//트랜짓타입/서치웨이-> 엔티티필드에없어서 변환x/ service단에서 처리
