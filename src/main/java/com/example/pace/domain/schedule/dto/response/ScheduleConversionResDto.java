package com.example.pace.domain.schedule.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleConversionResDto {
    private Long scheduleId;
    private Boolean isPathIncluded;
}
