package com.example.pace.domain.schedule.converter;

import com.example.pace.domain.schedule.dto.request.ScheduleReqDto;
import com.example.pace.domain.schedule.entity.Route;
import com.example.pace.domain.schedule.entity.RepeatRule;
import com.example.pace.domain.schedule.entity.Schedule;

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

}
