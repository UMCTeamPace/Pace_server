package com.example.pace.domain.schedule.controller;

import com.example.pace.domain.schedule.controller.docs.ScheduleConversionControllerDocs;
import com.example.pace.domain.schedule.dto.response.ScheduleConversionResDto;
import com.example.pace.domain.schedule.dto.response.ScheduleRouteDeleteResDto;
import com.example.pace.domain.schedule.exception.code.ScheduleSuccessCode;
import com.example.pace.domain.schedule.service.command.ScheduleRouteDeleteCommandService;
import com.example.pace.global.apiPayload.ApiResponse;
import com.example.pace.global.auth.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Schedule")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedule")
public class ScheduleConversionController implements ScheduleConversionControllerDocs {

    private final ScheduleRouteDeleteCommandService scheduleConversionService;

    @PatchMapping("/{id}/conversion")
    public ApiResponse<ScheduleRouteDeleteResDto> convertPathScheduleToNormal(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable("id") Long scheduleId
    ) {
        ScheduleRouteDeleteResDto resDto =
                scheduleConversionService.deleteScheduleRoute(user.member().getId(), scheduleId);

        return ApiResponse.onSuccess(ScheduleSuccessCode.SCHEDULE_CONVERTED, resDto);
    }
}
