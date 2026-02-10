package com.example.pace.domain.schedule.controller;

import com.example.pace.domain.schedule.dto.request.ScheduleRouteUpdateReqDto;
import com.example.pace.domain.schedule.dto.response.ScheduleRouteUpdateResDto;
import com.example.pace.domain.schedule.exception.code.ScheduleSuccessCode;
import com.example.pace.domain.schedule.service.command.ScheduleRouteUpdateCommandService;
import com.example.pace.global.apiPayload.ApiResponse;
import com.example.pace.global.auth.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Schedule")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedules")
public class ScheduleRouteUpdateController {

    private final ScheduleRouteUpdateCommandService scheduleRouteUpdateCommandService;

    @PutMapping("/{scheduleId}/route")
    public ApiResponse<ScheduleRouteUpdateResDto> updateScheduleRoute(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long scheduleId,
            @RequestBody ScheduleRouteUpdateReqDto request
    ) {
        Long memberId = customUserDetails.member().getId();

        ScheduleRouteUpdateResDto result =
                scheduleRouteUpdateCommandService.updateScheduleRoute(memberId, scheduleId, request);

        return ApiResponse.onSuccess(ScheduleSuccessCode.SCHEDULE_ROUTE_UPDATE_OK, result);
    }
}
