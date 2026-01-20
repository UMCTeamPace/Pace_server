package com.example.pace.domain.schedule.converter;

import com.example.pace.domain.schedule.dto.request.ScheduleReqDto;
import com.example.pace.domain.schedule.entity.Schedule;

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
}
