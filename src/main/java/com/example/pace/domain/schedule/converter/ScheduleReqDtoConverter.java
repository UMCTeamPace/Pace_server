package com.example.pace.domain.schedule.converter;

import com.example.pace.domain.schedule.dto.request.ScheduleReqDto;
import com.example.pace.domain.schedule.entity.Route;
import com.example.pace.domain.schedule.entity.RepeatRule;
import com.example.pace.domain.schedule.entity.RouteDetail;
import com.example.pace.domain.schedule.entity.Schedule;
import java.math.BigDecimal;

public class ScheduleReqDtoConverter {
    // 요청 dto -> Schedule 엔티티 객체
    public static Schedule toSchedule(ScheduleReqDto source) {
        return Schedule.builder()
                .title(source.getTitle())
                .isAllDay(source.getIsAllDay())
                .startDate(source.getStartDate())
                .endDate(source.getEndDate())
                .startTime(source.getStartTime())
                .endTime(source.getEndTime())
                .memo(source.getMemo())
                .isPathIncluded(source.getIsPathIncluded())
                .build();
    }

    // repeatDto -> RepeatRule 엔티티 객체
    public static RepeatRule toRepeatRule(ScheduleReqDto.RepeatDto repeatDto) {
        if (repeatDto == null) return null;

        return RepeatRule.builder()
                .repeatType(repeatDto.getRepeatType())
                .repeatInterval(repeatDto.getRepeatInterval())
                .daysOfWeek(repeatDto.getDaysOfWeek())
                .endType(repeatDto.getEndType())
                .endCount(repeatDto.getEndCount())
                .endDate(repeatDto.getRepeatEndDate())
                .build();
    }

    public static Route toRoute(ScheduleReqDto.RouteDto dto) {
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
                .isSaved(false)
                .build();
    }

    public static RouteDetail toRouteDetail(ScheduleReqDto.RouteDetailDto dto) {
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
                .build();
    }

    private static BigDecimal toBigDecimal(Double value) {
        return value != null ? BigDecimal.valueOf(value) : null;
    }

}
