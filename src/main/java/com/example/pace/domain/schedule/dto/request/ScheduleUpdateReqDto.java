package com.example.pace.domain.schedule.dto.request;

import com.example.pace.domain.schedule.dto.request.ScheduleReqDto.PlaceDto;
import com.example.pace.domain.schedule.dto.request.ScheduleReqDto.ReminderDto;
import com.example.pace.domain.schedule.dto.request.ScheduleReqDto.RepeatDto;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScheduleUpdateReqDto {
    private String title;
    private String memo;
    private Boolean isAllDay;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isPathIncluded;
    private RepeatDto repeatInfo;
    private PlaceDto place;
    private List<ReminderDto> reminders;

}