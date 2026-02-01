package com.example.pace.domain.schedule.controller;

import com.example.pace.domain.schedule.controller.ScheduleConversionControllerDocs;
import com.example.pace.domain.schedule.dto.response.ScheduleConversionResDto;
import com.example.pace.domain.schedule.exception.ScheduleSuccessCode;
import com.example.pace.domain.schedule.service.ScheduleConversionService;
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

    private final ScheduleConversionService scheduleConversionService;

    @PatchMapping("/{id}/conversion")
    public ApiResponse<ScheduleConversionResDto> convertPathScheduleToNormal(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable("id") Long scheduleId
    ) {
        ScheduleConversionResDto resDto =
                scheduleConversionService.convertPathScheduleToNormal(user.member().getId(), scheduleId);

        return ApiResponse.onSuccess(ScheduleSuccessCode.SCHEDULE_CONVERTED, resDto);
    }
}
