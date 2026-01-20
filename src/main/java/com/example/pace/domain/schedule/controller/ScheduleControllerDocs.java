package com.example.pace.domain.schedule.controller;

import com.example.pace.domain.schedule.dto.request.ScheduleReqDto;
import com.example.pace.domain.schedule.dto.response.ScheduleResDto;
import com.example.pace.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Schedule")
public interface ScheduleControllerDocs {

    @Operation(summary = "일정 생성", description = "새로운 일정을 생성하고 저장합니다.")
    ResponseEntity<ApiResponse<ScheduleResDto>> createSchedule(Long memberId, ScheduleReqDto request);
}