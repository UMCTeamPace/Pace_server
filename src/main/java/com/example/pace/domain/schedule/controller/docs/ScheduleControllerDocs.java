package com.example.pace.domain.schedule.controller.docs;

import com.example.pace.domain.schedule.dto.request.ScheduleDeleteReqDto;
import com.example.pace.domain.schedule.dto.request.ScheduleReqDto;
import com.example.pace.domain.schedule.dto.request.ScheduleUpdateReqDto;
import com.example.pace.domain.schedule.dto.response.ScheduleResDto;
import com.example.pace.domain.schedule.enums.UpdateScope;
import com.example.pace.global.apiPayload.ApiResponse;
import com.example.pace.global.auth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Schedule")
public interface ScheduleControllerDocs {

    @Operation(summary = "일정 생성", description = "새로운 일정을 생성하고 저장합니다.")
    ResponseEntity<ApiResponse<ScheduleResDto>> createSchedule(
            CustomUserDetails customUserDetails,
            ScheduleReqDto request);

    @Operation(summary = "일정 상세 조회")
    ResponseEntity<ApiResponse<ScheduleResDto>> getSchedule(
            CustomUserDetails customUserDetails,
            @Parameter(description = "조회할 일정의 ID")
            @PathVariable Long scheduleId);

    @Operation(summary = "일정 목록 조회")
    ResponseEntity<ApiResponse<Slice<ScheduleResDto>>> getScheduleList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            LocalDate startDate,
            LocalDate maxSearchDate,
            LocalDate lastDate,
            Long lastId
    );

    @Operation(summary = "일정 삭제")
    ResponseEntity<ApiResponse<String>> deleteSchedules(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody ScheduleDeleteReqDto request
    );

    @Operation(summary = "일정 수정")
    ResponseEntity<ApiResponse<ScheduleResDto>> updateSchedule(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long scheduleId,
            @RequestParam UpdateScope scope,
            ScheduleUpdateReqDto request
    );
}