package com.example.pace.domain.schedule.controller;

import com.example.pace.domain.schedule.dto.request.ScheduleRequestDto;
import com.example.pace.domain.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    // 일정 생성 API
    @PostMapping
    public ResponseEntity<Long> createSchedule(
            @RequestParam Long memberId, //임시
            @RequestBody ScheduleRequestDto requestDto
    ) {
        Long scheduleId = scheduleService.createSchedule(memberId, requestDto);

        return ResponseEntity.ok(scheduleId);
    }
}