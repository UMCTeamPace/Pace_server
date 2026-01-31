package com.example.pace.domain.schedule.controller;

import com.example.pace.domain.schedule.dto.request.ScheduleRouteUpdateReqDto;
import com.example.pace.domain.schedule.dto.response.ScheduleRouteUpdateResDto;
import com.example.pace.domain.schedule.exception.ScheduleSuccessCode;
import com.example.pace.domain.schedule.service.ScheduleRouteUpdateService;
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

    private final ScheduleRouteUpdateService scheduleRouteUpdateService;

    @PutMapping("/{scheduleId}/route")
    public ApiResponse<ScheduleRouteUpdateResDto> updateScheduleRoute(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long scheduleId,
            @RequestBody ScheduleRouteUpdateReqDto request
    ) {
        Long memberId = customUserDetails.member().getId();

        ScheduleRouteUpdateResDto result =
                scheduleRouteUpdateService.updateScheduleRoute(memberId, scheduleId, request);

        return ApiResponse.onSuccess(ScheduleSuccessCode.SCHEDULE_ROUTE_UPDATE_OK, result);
    }
}
