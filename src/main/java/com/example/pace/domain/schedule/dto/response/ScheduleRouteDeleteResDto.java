package com.example.pace.domain.schedule.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "일정 경로 삭제 응답 DTO")
public class ScheduleRouteDeleteResDto {

    private Long scheduleId;
    private LocalDateTime updatedAt;

    public static ScheduleRouteDeleteResDto of(Long scheduleId, LocalDateTime updatedAt) {
        return ScheduleRouteDeleteResDto.builder()
                .scheduleId(scheduleId)
                .updatedAt(updatedAt)
                .build();
    }
}
