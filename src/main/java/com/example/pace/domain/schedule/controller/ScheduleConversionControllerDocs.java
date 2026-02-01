package com.example.pace.domain.schedule.controller;

import com.example.pace.domain.schedule.dto.response.ScheduleConversionResDto;
import com.example.pace.global.apiPayload.ApiResponse;
import com.example.pace.global.auth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Schedule")
public interface ScheduleConversionControllerDocs {

    @Operation(summary = "경로 일정 -> 일반 일정 전환")
    @PatchMapping("/{id}/conversion")
    ApiResponse<ScheduleConversionResDto> convertPathScheduleToNormal(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable("id") Long scheduleId
    );
}
