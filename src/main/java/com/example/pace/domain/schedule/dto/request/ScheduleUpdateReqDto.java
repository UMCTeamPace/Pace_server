package com.example.pace.domain.schedule.dto.request;

import com.example.pace.domain.schedule.dto.request.ScheduleReqDto.PlaceDto;
import com.example.pace.domain.schedule.dto.request.ScheduleReqDto.ReminderDto;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleUpdateReqDto {
    private String title;
    private String memo;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String calendarId;
    private String color;
    private Boolean isPathIncluded;
    private PlaceDto place;
    private List<ReminderDto> reminders;

}