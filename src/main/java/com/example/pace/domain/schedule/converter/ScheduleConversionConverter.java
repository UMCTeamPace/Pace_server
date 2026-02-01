package com.example.pace.domain.schedule.converter;
import com.example.pace.domain.schedule.dto.response.ScheduleConversionResDto;
import com.example.pace.domain.schedule.entity.Schedule;

public class ScheduleConversionConverter {
    public static ScheduleConversionResDto toConversionResponse(Schedule schedule) {
        return ScheduleConversionResDto.builder()
                .scheduleId(schedule.getId())
                .isPathIncluded(schedule.getIsPathIncluded())
                .build();
    }
}
