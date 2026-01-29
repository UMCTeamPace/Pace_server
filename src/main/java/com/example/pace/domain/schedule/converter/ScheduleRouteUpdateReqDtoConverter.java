package com.example.pace.domain.schedule.converter;

import com.example.pace.domain.schedule.dto.request.ScheduleRouteUpdateReqDto;
import com.example.pace.domain.schedule.entity.Route;
import com.example.pace.domain.schedule.entity.RouteDetail;
import com.example.pace.domain.schedule.entity.Schedule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ScheduleRouteUpdateReqDtoConverter {

    public static Route toRoute(ScheduleRouteUpdateReqDto req) {
        return Route.builder()
                .originName(req.getOrigin().getOriginName())
                .originLat(req.getOrigin().getOriginLat() != null
                        ? BigDecimal.valueOf(req.getOrigin().getOriginLat()) : null)
                .originLng(req.getOrigin().getOriginLng() != null
                        ? BigDecimal.valueOf(req.getOrigin().getOriginLng()) : null)
                .destName(req.getDest().getDestName())
                .destLat(req.getDest().getDestLat() != null
                        ? BigDecimal.valueOf(req.getDest().getDestLat()) : null)
                .destLng(req.getDest().getDestLng() != null
                        ? BigDecimal.valueOf(req.getDest().getDestLng()) : null)
                .totalTime(req.getTotalTime())
                .totalDistance(req.getTotalDistance())
                .isSaved(false)
                .build();
    }

    public static RouteDetail toRouteDetail(ScheduleRouteUpdateReqDto.RouteDetailDto dto) {
        return RouteDetail.builder()
                .sequence(dto.getSequence())
                .duration(dto.getDuration())
                .distance(dto.getDistance())
                .description(dto.getDescription())
                .transitType(dto.getTransitType())
                .lineName(dto.getLineName())
                .lineColor(dto.getLineColor())
                .stopCount(dto.getStopCount())
                .departureStop(dto.getDepartureStop())
                .arrivalStop(dto.getArrivalStop())
                .startLat(dto.getStartLat() != null ? BigDecimal.valueOf(dto.getStartLat()) : null)
                .startLng(dto.getStartLng() != null ? BigDecimal.valueOf(dto.getStartLng()) : null)
                .endLat(dto.getEndLat() != null ? BigDecimal.valueOf(dto.getEndLat()) : null)
                .endLng(dto.getEndLng() != null ? BigDecimal.valueOf(dto.getEndLng()) : null)
                .build();
    }
}