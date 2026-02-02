package com.example.pace.domain.schedule.controller;

import com.example.pace.domain.schedule.dto.request.ScheduleDeleteReqDto;
import com.example.pace.domain.schedule.dto.request.ScheduleReqDto;
import com.example.pace.domain.schedule.dto.request.ScheduleUpdateReqDto;
import com.example.pace.domain.schedule.dto.response.ScheduleResDto;
import com.example.pace.domain.schedule.enums.UpdateScope;
import com.example.pace.domain.schedule.exception.ScheduleSuccessCode;
import com.example.pace.domain.schedule.service.ScheduleService;
import com.example.pace.global.apiPayload.ApiResponse;
import com.example.pace.global.auth.CustomUserDetails;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody ScheduleReqDto requestDto
    ) {
        Long memberId = customUserDetails.member().getId();
        ScheduleResDto responseDto= scheduleService.createSchedule(memberId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.onSuccess(ScheduleSuccessCode.SCHEDULE_CREATE_OK, responseDto));
    }

    //일정 상세조회 API
    @Override
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ApiResponse<ScheduleResDto>> getSchedule(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long scheduleId
    ) {
        Long  memberId = customUserDetails.member().getId();
        ScheduleResDto responseDto = scheduleService.getSchedule(memberId,scheduleId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.onSuccess(ScheduleSuccessCode.SCHEDULE_GET_OK, responseDto));
    }

    //일정 목록조회 API
    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<Slice<ScheduleResDto>>> getScheduleList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate endDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate lastDate,
            @RequestParam(required = false) Long lastId
    ){
        Long memberId = customUserDetails.member().getId();
        LocalDate maxSearchDate = (endDate != null) ? endDate : LocalDate.of(9999, 12, 31);
        Slice<ScheduleResDto> responseDto = scheduleService.getScheduleList(memberId, startDate, maxSearchDate, lastDate, lastId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.onSuccess(ScheduleSuccessCode.SCHEDULE_GET_OK, responseDto));

    }

    //일정 삭제 API
    @Override
    @DeleteMapping("")
    public ResponseEntity<ApiResponse<String>> deleteSchedules(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody ScheduleDeleteReqDto request
    ){
        Long memberId = customUserDetails.member().getId();
        scheduleService.deleteSchedules(memberId, request.scheduleIds());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.onSuccess(ScheduleSuccessCode.SCHEDULE_DELETE_OK,null));
    }

    //일정 수정 API
    @Override
    @PatchMapping("/{scheduleId}")
    public ResponseEntity<ApiResponse<ScheduleResDto>> updateSchedule(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long scheduleId,
            @RequestParam(defaultValue = "SINGLE") UpdateScope scope,
            @RequestBody ScheduleUpdateReqDto requestDto
    ){
        Long memberId = customUserDetails.member().getId();
        ScheduleResDto responseDto = scheduleService.updateSchedule(memberId, scheduleId, requestDto, scope);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.onSuccess(ScheduleSuccessCode.SCHEDULE_UPDATE_OK,responseDto));

    }
}
