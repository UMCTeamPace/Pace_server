package com.example.pace.domain.schedule.controller;

import com.example.pace.domain.schedule.dto.request.ScheduleRequestDto;
import com.example.pace.domain.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedules")
@Tag(name = "Schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    // 일정 생성 API
    @PostMapping
    @Operation(summary = "일정 생성", description = "새로운 일정을 생성하고 저장합니다.")
    public ResponseEntity<Long> createSchedule(
            @RequestParam Long memberId, //임시
            @RequestBody ScheduleRequestDto requestDto
    ) {
        Long scheduleId = scheduleService.createSchedule(memberId, requestDto);

        return ResponseEntity.ok(scheduleId);
    }
}
