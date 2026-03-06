package com.example.pace.domain.schedule.converter;

import com.example.pace.domain.schedule.dto.request.ScheduleReqDto;
import com.example.pace.domain.schedule.dto.request.ScheduleRouteUpdateReqDto;
import com.example.pace.domain.schedule.dto.request.TransitDetailDto;
import com.example.pace.domain.schedule.dto.response.ScheduleResDto;
import com.example.pace.domain.schedule.dto.response.ScheduleRouteDeleteResDto;
import com.example.pace.domain.schedule.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Comparator.comparing;

@Component
@RequiredArgsConstructor
public class ScheduleConverter {

    public Route toRoute(ScheduleReqDto.RouteReqDto dto) {
        if (dto == null) return null;
        return Route.builder()
                .originName(dto.getOriginName())
                .originLat(dto.getOriginLat())
                .originLng(dto.getOriginLng())
                .destName(dto.getDestName())
                .destLat(dto.getDestLat())
                .destLng(dto.getDestLng())
                .totalTime(dto.getTotalTime())
                .totalDistance(dto.getTotalDistance())
                .arrivalTime(dto.getArrivalTime())
                .departureTime(dto.getDepartureTime())
                .isSaved(false)
                .build();
    }

    public RouteDetail toRouteDetail(ScheduleReqDto.RouteDetailReqDto dto) {
        if (dto == null) return null;
        return RouteDetail.builder()
                .sequence(dto.getSequence())
                .startLat(dto.getStartLat())
                .startLng(dto.getStartLng())
                .endLat(dto.getEndLat())
                .endLng(dto.getEndLng())
                .duration(dto.getDuration())
                .distance(dto.getDistance())
                .description(dto.getDescription())
                .points(dto.getPoints())
                .transitDetail(toTransitDetail(dto.getTransitDetail())) // 중첩 객체 변환
                .build();

    }

    public Route toRoute(ScheduleRouteUpdateReqDto req) {
        return Route.builder()
                .originName(req.getOriginName())
                .originLat(req.getOriginLat())
                .originLng(req.getOriginLng())
                .destName(req.getDestName())
                .destLat(req.getDestLat())
                .destLng(req.getDestLng())
                .totalTime(req.getTotalTime())
                .totalDistance(req.getTotalDistance())
                .arrivalTime(req.getArrivalTime())
                .departureTime(req.getDepartureTime())
                .isSaved(false)
                .build();
    }

    public RouteDetail toRouteDetail(ScheduleRouteUpdateReqDto.RouteDetailUpdateReqDto dto) {
        if (dto == null) return null;
        return RouteDetail.builder()
                .sequence(dto.getSequence())
                .startLat(dto.getStartLat())
                .startLng(dto.getStartLng())
                .endLat(dto.getEndLat())
                .endLng(dto.getEndLng())
                .duration(dto.getDuration())
                .distance(dto.getDistance())
                .description(dto.getDescription())
                .points(dto.getPoints())
                .transitDetail(toTransitDetail(dto.getTransitDetail()))
                .build();
    }

    // Entity -> Response DTO
    public ScheduleResDto toScheduleResDto(Schedule schedule) {
        return ScheduleResDto.builder()
                .scheduleId(Boolean.TRUE.equals(schedule.getIsPathIncluded()) ?
                        schedule.getId() * -1 : schedule.getId())
                .scheduleInfo(toInfoDto(schedule))
                .place(toPlaceDto(schedule.getPlace()))
                .route(toRouteDto(schedule.getRoute()))
                .reminders(toReminderDtos(schedule.getReminderList()))
                .build();
    }

    public ScheduleRouteDeleteResDto toScheduleRouteDeleteResDto(Schedule schedule) {
        return ScheduleRouteDeleteResDto.of(schedule.getId()*-1, schedule.getUpdatedAt());
    }

    // 기본 정보
    private ScheduleResDto.ScheduleInfoDto toInfoDto(Schedule schedule) {
        return ScheduleResDto.ScheduleInfoDto.builder()
                .title(schedule.getTitle())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .calendarId(schedule.getCalendarId())
                .color(schedule.getColor())
                .memo(schedule.getMemo())
                .build();
    }

    // 장소 정보
    private ScheduleResDto.PlaceDto toPlaceDto(Place place) {
        if (place == null) return null;
        return ScheduleResDto.PlaceDto.builder()
                .targetName(place.getTargetName())
                .targetLat(place.getTargetLat())
                .targetLng(place.getTargetLng())
                .build();
    }

    // 경로 정보
    private ScheduleResDto.RouteResDto toRouteDto(Route route) {
        if (route == null) return null;
        return ScheduleResDto.RouteResDto.builder()
                .originName(route.getOriginName())
                .originLat(route.getOriginLat())
                .originLng(route.getOriginLng())
                .destName(route.getDestName())
                .destLat(route.getDestLat())
                .destLng(route.getDestLng())
                .totalTime(route.getTotalTime())
                .totalDistance(route.getTotalDistance())
                .arrivalTime(route.getArrivalTime())
                .departureTime(route.getDepartureTime())
                .routeDetails(toDetailDtos(route.getRouteDetails()))
                .build();
    }

    // 상세 경로
    private List<ScheduleResDto.RouteDetailResDto> toDetailDtos(List<RouteDetail> details) {
        if (details == null) return List.of();
        return details.stream()
                .sorted(comparing(RouteDetail::getSequence))
                .map(this::toRouteDetailDto)
                .toList();
    }

    // 개별 상세 경로
    private ScheduleResDto.RouteDetailResDto toRouteDetailDto(RouteDetail detail) {
        if (detail == null) return null;
        return ScheduleResDto.RouteDetailResDto.builder()
                .sequence(detail.getSequence())
                .startLat(detail.getStartLat())
                .startLng(detail.getStartLng())
                .endLat(detail.getEndLat())
                .endLng(detail.getEndLng())
                .duration(detail.getDuration())
                .distance(detail.getDistance())
                .description(detail.getDescription())
                .points(detail.getPoints())
                .transitDetail(toTransitDetailResDto(detail.getTransitDetail())) // 응답용 헬퍼 사용
                .build();
    }

    private TransitDetail toTransitDetail(TransitDetailDto dto) {
        if (dto == null) return null;
        return TransitDetail.builder()
                .transitType(dto.getTransitType())
                .lineName(dto.getLineName())
                .lineColor(dto.getLineColor())
                .stopCount(dto.getStopCount())
                .departureStop(dto.getDepartureStop())
                .arrivalStop(dto.getArrivalStop())
                .departureTime(dto.getDepartureTime())
                .arrivalTime(dto.getArrivalTime())
                .shortName(dto.getShortName())
                .locationLat(dto.getLocationLat())
                .locationLng(dto.getLocationLng())
                .headsign(dto.getHeadsign())
                .stationPath(dto.getStationPath())
                .build();
    }

    private ScheduleResDto.TransitDetailResDto toTransitDetailResDto(TransitDetail detail) {
        if (detail == null) return null;
        return ScheduleResDto.TransitDetailResDto.builder()
                .transitType(detail.getTransitType())
                .lineName(detail.getLineName())
                .lineColor(detail.getLineColor())
                .stopCount(detail.getStopCount())
                .departureStop(detail.getDepartureStop())
                .arrivalStop(detail.getArrivalStop())
                .departureTime(detail.getDepartureTime())
                .arrivalTime(detail.getArrivalTime())
                .shortName(detail.getShortName())
                .locationLat(detail.getLocationLat())
                .locationLng(detail.getLocationLng())
                .headsign(detail.getHeadsign())
                .stationPath(detail.getStationPath())
                .build();
    }
    // 알림 정보
    private List<ScheduleResDto.ReminderDto> toReminderDtos(List<Reminder> reminders) {
        if (reminders == null) return List.of();
        return reminders.stream()
                .map(this::toReminderDto)
                .toList();
    }

    private ScheduleResDto.ReminderDto toReminderDto(Reminder reminder) {
        return ScheduleResDto.ReminderDto.builder()
                .reminderType(reminder.getReminderType())
                .minutesBefore(reminder.getMinutesBefore())
                .reminderEnabled(reminder.getReminderEnabled())
                .build();
    }

}