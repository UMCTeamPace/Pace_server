package com.example.pace.domain.schedule.converter;

import com.example.pace.domain.schedule.dto.request.ScheduleRouteUpdateReqDto;
import com.example.pace.domain.schedule.entity.Route;
import com.example.pace.domain.schedule.entity.RouteDetail;
import com.example.pace.domain.schedule.entity.Schedule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ScheduleRouteUpdateReqDtoConverter {

    public static Route toRoute(ScheduleRouteUpdateReqDto req, Schedule schedule) {
        return Route.builder()
                .originName(req.getOrigin().getOriginName())
                .originLat(toBigDecimal(req.getOrigin().getOriginLat()))
                .originLng(toBigDecimal(req.getOrigin().getOriginLng()))
                .destName(req.getDest().getDestName())
                .destLat(toBigDecimal(req.getDest().getDestLat()))
                .destLng(toBigDecimal(req.getDest().getDestLng()))
                .totalTime(req.getTotalTime())
                .totalDistance(req.getTotalDistance())
                .isSaved(false)
                .schedule(schedule)
                .routeDetails(new ArrayList<>())
                .build();
    }

    //route(step) 리스트 -> RouteDetail 리스트

    public static List<RouteDetail> toRouteDetails(
            Route route,
            List<ScheduleRouteUpdateReqDto.RouteDetailDto> dtos) {

        if (dtos == null || dtos.isEmpty()) return List.of();

        List<RouteDetail> details = new ArrayList<>();
        for (ScheduleRouteUpdateReqDto.RouteDetailDto d : dtos) {
            RouteDetail detail = RouteDetail.builder()
                    .route(route)
                    .sequence(d.getSequence())
                    .startLat(toBigDecimal(d.getStartLat()))
                    .startLng(toBigDecimal(d.getStartLng()))
                    .endLat(toBigDecimal(d.getEndLat()))
                    .endLng(toBigDecimal(d.getEndLng()))
                    .transitType(d.getTransitType())
                    .duration(d.getDuration())
                    .distance(d.getDistance())
                    .description(d.getDescription())
                    .lineName(d.getLineName())
                    .lineColor(d.getLineColor())
                    .stopCount(d.getStopCount())
                    .departureStop(d.getDepartureStop())
                    .arrivalStop(d.getArrivalStop())
                    .build();

            details.add(detail);
        }
        return details;
    }

    private static BigDecimal toBigDecimal(Double value) {
        return value == null ? null : BigDecimal.valueOf(value);
    }
}