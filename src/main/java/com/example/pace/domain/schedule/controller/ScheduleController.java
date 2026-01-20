package com.example.pace.domain.schedule.controller;

import com.example.pace.domain.schedule.dto.request.ScheduleReqDto;
import com.example.pace.domain.schedule.dto.response.ScheduleResDto;
import com.example.pace.domain.schedule.service.ScheduleService;
import com.example.pace.global.apiPayload.ApiResponse;
import com.example.pace.global.apiPayload.code.GeneralSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedules")
public class ScheduleController implements ScheduleControllerDocs {

    private final ScheduleService scheduleService;

    // 일정 생성 API
    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<ScheduleResDto>> createSchedule(
            @RequestParam Long memberId, //임시
            @RequestBody ScheduleReqDto requestDto
    ) {
        Long scheduleId = scheduleService.createSchedule(memberId, requestDto);
        ScheduleResDto responseDto = ScheduleResDto.of(scheduleId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.onSuccess(GeneralSuccessCode.OK, responseDto));
    }
}
