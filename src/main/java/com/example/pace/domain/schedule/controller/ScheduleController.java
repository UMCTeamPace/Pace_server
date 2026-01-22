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
        ScheduleResDto responseDto= scheduleService.createSchedule(memberId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.onSuccess(GeneralSuccessCode.OK, responseDto));
    }

    //일정 상세조회 API
    @Override
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ApiResponse<ScheduleResDto>> getSchedule(
            @PathVariable Long scheduleId
    ) {
        ScheduleResDto responseDto = scheduleService.getSchedule(scheduleId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.onSuccess(GeneralSuccessCode.OK,responseDto));
    }


}
