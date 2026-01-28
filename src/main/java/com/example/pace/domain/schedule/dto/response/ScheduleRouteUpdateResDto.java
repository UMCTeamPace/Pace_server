package com.example.pace.domain.schedule.dto.response;

import com.example.pace.domain.schedule.entity.Schedule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "일정 경로 수정 응답 DTO")
public class ScheduleRouteUpdateResDto {

    private Long scheduleId;
    private Long routeId;
    private LocalDateTime updatedAt;

    public static ScheduleRouteUpdateResDto from(Schedule schedule) {
        return ScheduleRouteUpdateResDto.builder()
                .scheduleId(schedule.getId())
                .routeId(schedule.getRoute() != null ? schedule.getRoute().getId() : null)
                .updatedAt(schedule.getUpdatedAt())
                .build();
    }

    //경로 수정 후 scheduleId, routeId, 수정 시각을 반환
    @Getter
    @Builder
    @Schema(description = "일정 경로 수정 최소 응답 DTO")
    public static class Simple {
        private Long scheduleId;
        private Long routeId;
        private LocalDateTime updatedAt;

        public static Simple from(Schedule schedule) {
            return Simple.builder()
                    .scheduleId(schedule.getId())
                    .routeId(schedule.getRoute() != null ? schedule.getRoute().getId() : null)
                    .updatedAt(schedule.getUpdatedAt())
                    .build();
        }
    }
}