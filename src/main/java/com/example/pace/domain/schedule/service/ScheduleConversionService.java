package com.example.pace.domain.schedule.service;

import com.example.pace.domain.schedule.dto.response.ScheduleConversionResDto;

public interface ScheduleConversionService {
    ScheduleConversionResDto convertPathScheduleToNormal(Long memberId, Long scheduleId);
}
