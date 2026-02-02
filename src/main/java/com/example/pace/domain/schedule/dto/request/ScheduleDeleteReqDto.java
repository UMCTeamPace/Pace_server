package com.example.pace.domain.schedule.dto.request;

import java.util.List;

public record ScheduleDeleteReqDto(
        List<Long> scheduleIds
) {}
