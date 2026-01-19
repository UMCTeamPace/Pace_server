package com.example.pace.domain.schedule.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScheduleResDto {
    private Long scheduleId;

    public static ScheduleResDto of(Long scheduleId) {
        return ScheduleResDto.builder()
                .scheduleId(scheduleId)
                .build();
    }
}