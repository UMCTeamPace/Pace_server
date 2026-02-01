package com.example.pace.domain.schedule.controller;

import com.example.pace.domain.schedule.dto.response.ScheduleConversionResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

public interface ScheduleConversionControllerDocs {

    @Operation(
            summary = "경로 일정을 일반 일정으로 전환",
            description = "경로 일정의 isPathIncluded를 false로 변경하고 연결된 Route 정보를 hard delete 합니다."
    )
    ScheduleConversionResDto convertPathScheduleToNormal(
            @Parameter(description = "일정 ID", required = true)
            Long scheduleId
    );
}
