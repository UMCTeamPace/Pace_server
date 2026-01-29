package com.example.pace.domain.schedule.converter;

import com.example.pace.domain.schedule.dto.request.ScheduleReqDto;
import com.example.pace.domain.schedule.dto.request.ScheduleRouteUpdateReqDto;
import com.example.pace.domain.schedule.entity.Route;
import com.example.pace.domain.schedule.entity.RouteDetail;
import com.example.pace.domain.schedule.entity.Schedule;
import java.util.List;

public class ScheduleReqDtoConverter {
    // 요청 dto -> Schedule 엔티티 객체
    public static Schedule toSchedule(ScheduleReqDto source) {
        return Schedule.builder()
                .title(source.getTitle())
                .startDate(source.getStartDate())
                .endDate(source.getEndDate())
                .startTime(source.getStartTime())
                .endTime(source.getEndTime())
                .memo(source.getMemo())
                .isPathIncluded(source.getIsPathIncluded())
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

}
