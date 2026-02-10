package com.example.pace.domain.schedule.converter;

import com.example.pace.domain.schedule.dto.request.ScheduleRouteUpdateReqDto;
import com.example.pace.domain.schedule.entity.Route;
import com.example.pace.domain.schedule.entity.RouteDetail;
import java.math.BigDecimal;

public class ScheduleRouteUpdateReqDtoConverter {

    public static Route toRoute(ScheduleRouteUpdateReqDto req) {
        return Route.builder()
                .originName(req.getOrigin().getOriginName())
                .originLat(toBigDecimal(req.getOrigin().getOriginLat()))
                .originLng(toBigDecimal(req.getOrigin().getOriginLng()))
                .destName(req.getDest().getDestName())
                .destLat(toBigDecimal(req.getDest().getDestLat()))
                .destLng(toBigDecimal(req.getDest().getDestLng()))
                .totalTime(req.getTotalTime())
                .totalDistance(req.getTotalDistance())
                .arrivalTime(req.getArrivalTime())
                .departureTime(req.getDepartureTime())
                .isSaved(false)
                .build();
    }

    private static BigDecimal toBigDecimal(Double value) {
        return value != null ? BigDecimal.valueOf(value) : null;
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
                .startLat(toBigDecimal(dto.getStartLat()))
                .startLng(toBigDecimal(dto.getStartLng()))
                .endLat(toBigDecimal(dto.getEndLat()))
                .endLng(toBigDecimal(dto.getEndLng()))
                .shortName(dto.getShortName())
                .build();
    }
}