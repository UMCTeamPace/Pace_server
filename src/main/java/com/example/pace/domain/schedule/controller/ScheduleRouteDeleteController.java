package com.example.pace.domain.schedule.controller;

import com.example.pace.domain.schedule.dto.response.ScheduleRouteDeleteResDto;
import com.example.pace.domain.schedule.exception.ScheduleSuccessCode;
import com.example.pace.domain.schedule.service.ScheduleRouteDeleteService;
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
public class ScheduleRouteDeleteController {

    private final ScheduleRouteDeleteService scheduleRouteDeleteService;

    @DeleteMapping("/{scheduleId}/route")
    public ApiResponse<ScheduleRouteDeleteResDto> deleteScheduleRoute(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long scheduleId
    ) {
        Long memberId = customUserDetails.member().getId();

        ScheduleRouteDeleteResDto result =
                scheduleRouteDeleteService.deleteScheduleRoute(scheduleId, memberId);

        return ApiResponse.onSuccess(ScheduleSuccessCode.SCHEDULE_ROUTE_DELETE_OK, result);
    }
}
