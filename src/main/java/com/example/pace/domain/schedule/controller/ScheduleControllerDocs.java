package com.example.pace.domain.schedule.controller;

import com.example.pace.domain.schedule.dto.request.ScheduleReqDto;
import com.example.pace.domain.schedule.dto.response.ScheduleResDto;
import com.example.pace.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Schedule")
public interface ScheduleControllerDocs {

    @Operation(summary = "일정 생성", description = "새로운 일정을 생성하고 저장합니다.")
    ResponseEntity<ApiResponse<ScheduleResDto>> createSchedule(Long memberId, ScheduleReqDto request);

    @Operation(summary = "일정 상세 조회", description = "일정 Id를 통해 일정 상세정보를 조회합니다.")
    @GetMapping("/{scheduleId}")
    ResponseEntity<ApiResponse<ScheduleResDto>> getSchedule(
            @Parameter(description = "조회할 일정의 ID")
            @PathVariable Long scheduleId);
}